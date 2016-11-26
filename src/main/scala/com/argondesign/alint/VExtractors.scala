package com.argondesign.alint

import org.antlr.v4.runtime.ParserRuleContext

trait VExtractors {
  object ResetClauseExtractor extends Visitor[Option[ParserRuleContext]](None, (a, b) => if (a.isEmpty) b else a) {
    override def visitIfStatement(ctx: IfStatementContext) = {
      object HasReset extends Visitor[Boolean](false, _ || _) {
        override def visitExprIdentifier(ctx: ExprIdentifierContext) = SignalNames.isReset(ctx.text)
      }

      if (HasReset(ctx.ifCond)) {
        Some(ctx.ifBlock)
      } else if (ctx.elifConds exists (HasReset(_))) {
        Some(ctx.elifBlocks(ctx.elifConds indexWhere (HasReset(_))))
      } else {
        visitChildren(ctx)
      }
    }
  }

  implicit class ParserRuleContextExtractor(val ctx: ParserRuleContext) {
    def resetClause() = ResetClauseExtractor(ctx)
  }
}

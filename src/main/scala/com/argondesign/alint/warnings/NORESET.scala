package com.argondesign.alint.warnings

import com.argondesign.alint.Loc
import com.argondesign.alint.Source
import com.argondesign.alint.SourceAnalyser
import com.argondesign.alint.SourceWarning
import com.argondesign.alint.Visitor
import org.antlr.v4.runtime.ParserRuleContext
import com.argondesign.alint.SignalNames

final case class NORESET(val loc: Loc, name: String) extends SourceWarning {
  val message = s"Signal '$name' not assigned in reset clasue"
}

object NORESET extends SourceAnalyser[List[NORESET]] {
  object GetResetClause extends Visitor[ParserRuleContext](null, (a, b) => if (a == null) b else a) {
    override def visitIfStatement(ctx: IfStatementContext) = {
      object HasReset extends Visitor[Boolean](false, _ || _) {
        override def visitExprIdentifier(ctx: ExprIdentifierContext) = SignalNames.isReset(ctx.text)
      }

      if (HasReset(ctx.ifCond)) {
        ctx.ifBlock
      } else if (ctx.elifConds exists (HasReset(_))) {
        ctx.elifBlocks(ctx.elifConds indexWhere (HasReset(_)))
      } else {
        visitChildren(ctx)
      }
    }
  }

  object NORESETSourceAnalyserVisitor extends WarningsSourceAnalyserVisitor[NORESET] {
    override def visitAlwaysEvent(ctx: AlwaysEventContext) = {
      val resetClauseCtx = GetResetClause(ctx.statement)
      if (resetClauseCtx != null) {
        object GetNBATargets extends Visitor[List[ParserRuleContext]](Nil, _ ::: _) {
          override def visitNonblockingAssignment(ctx: NonblockingAssignmentContext) = {
            object GetIdentifiers extends Visitor[List[ParserRuleContext]](Nil, _ ::: _) {
              override def visitHierarchicalIdentifier(ctx: HierarchicalIdentifierContext) = List(ctx)
            }
            GetIdentifiers(ctx.variableLvalue)
          }
        }

        val allTargets = GetNBATargets(ctx.statement)
        val resetNames = allTargets filter (_ -<- resetClauseCtx) map (_.text)
        val otherTargets = allTargets filter (_ !<- resetClauseCtx)

        for (target <- otherTargets; if !resetNames.contains(target.text))
          yield NORESET(target.loc, target.text)
      } else {
        Nil
      }
    }
  }

  def apply(source: Source) = NORESETSourceAnalyserVisitor(source)
}

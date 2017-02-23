package com.argondesign.alint.warnings

import com.argondesign.alint.Loc
import com.argondesign.alint.Source
import com.argondesign.alint.SourceAnalyser
import com.argondesign.alint.SourceWarning
import com.argondesign.alint.Visitor
import com.argondesign.alint.antlr4.VParser.ExprIdentifierContext
import org.antlr.v4.runtime.tree.RuleNode

final case class BEGINEND(val loc: Loc, name: String) extends SourceWarning {
  val message = s"Body of '$name' should have begin/end"
}

object BEGINEND extends SourceAnalyser[List[BEGINEND]] {
  class WarnIn(val name: String) extends WarningsSourceAnalyserVisitor[BEGINEND] {
    override def visitStmtSeqBlock(ctx: StmtSeqBlockContext) = Nil
    override def visitStmtBlockAssign(ctx: StmtBlockAssignContext) = List(BEGINEND(ctx.loc, name))
    override def visitStmtNonBlockAssign(ctx: StmtNonBlockAssignContext) = List(BEGINEND(ctx.loc, name))
    override def visitStmtContAssign(ctx: StmtContAssignContext) = List(BEGINEND(ctx.loc, name))
    override def visitStmtCase(ctx: StmtCaseContext) = List(BEGINEND(ctx.loc, name))
    override def visitStmtIf(ctx: StmtIfContext) = List(BEGINEND(ctx.loc, name))
    override def visitStmtLoop(ctx: StmtLoopContext) = List(BEGINEND(ctx.loc, name))
    override def visitStmtSystemTaskCall(ctx: StmtSystemTaskCallContext) = List(BEGINEND(ctx.loc, name))
    override def visitStmtTaskCall(ctx: StmtTaskCallContext) = List(BEGINEND(ctx.loc, name))
  }
  object WarnIn {
    def apply(name: String) = new WarnIn(name)
  }

  object BEGINENDSourceAnalyserVisitor extends WarningsSourceAnalyserVisitor[BEGINEND] {
    override def visitAlwaysAtStar(ctx: AlwaysAtStarContext) = {
      WarnIn("always @*")(ctx.statement) ::: visitChildren(ctx)
    }
    override def visitAlwaysEvent(ctx: AlwaysEventContext) = {
      WarnIn("always @(...)")(ctx.statement) ::: visitChildren(ctx)
    }
    override def visitIfStatement(ctx: IfStatementContext) = {
      WarnIn("if (...)")(ctx.ifBlock) :::
        WarnIn("else if (...)")(ctx.elifBlocks) :::
        WarnIn("else")(ctx.elseBlock) :::
        visitChildren(ctx)
    }
    override def visitLoopForStatement(ctx: LoopForStatementContext) = {
      WarnIn("for")(ctx.statement) ::: visitChildren(ctx)
    }
  }

  def apply(source: Source) = BEGINENDSourceAnalyserVisitor(source)
}

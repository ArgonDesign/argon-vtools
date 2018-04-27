////////////////////////////////////////////////////////////////////////////////
// Argon Design Ltd.
// Copyright (c) 2016-2018 Argon Design Ltd. All rights reserved.
//
// This file is covered by the BSD (with attribution) license.
// See the LICENSE file for the precise wording of the license.
//
// Module : Alint
// Author : Geza Lore
//
// DESCRIPTION:
//
////////////////////////////////////////////////////////////////////////////////

package com.argondesign.alint.warnings

import com.argondesign.alint.Loc
import com.argondesign.alint.Source
import com.argondesign.alint.SourceAnalyser
import com.argondesign.alint.SourceWarning
import com.argondesign.alint.SourceAnalyser
import com.argondesign.alint.antlr4.VParser.ExprIdentifierContext
import com.argondesign.alint.SignalNames
import com.argondesign.alint.Visitor
import scala.collection.mutable.ListBuffer
import org.antlr.v4.runtime.tree.RuleNode

final case class RESETSTYLE(val loc: Loc) extends SourceWarning {
  val message = "Non-standard reset style"
}

object RESETSTYLE extends SourceAnalyser[List[RESETSTYLE]] {
  object CollectResetNames extends Visitor[List[String]](Nil, _ ::: _) {
    override def visitExprIdentifier(ctx: ExprIdentifierContext) = {
      if (SignalNames.isReset(ctx.text)) List(ctx.text) else Nil
    }
  }

  object WarnIn extends WarningsSourceAnalyserVisitor[RESETSTYLE] {
    override def visitIfStatement(ctx: IfStatementContext) = {
      val ifWarning = if (!CollectResetNames(ctx.ifCond).isEmpty) List(RESETSTYLE(ctx.ifCond.loc)) else Nil
      val elifWarnings = for (elifCond <- ctx.elifConds.toList; if (!CollectResetNames(elifCond).isEmpty))
        yield RESETSTYLE(elifCond.loc)

      ifWarning ::: elifWarnings ::: visitChildren(ctx)
    }
  }

  object AlwaysStatementVisitor extends WarningsSourceAnalyserVisitor[RESETSTYLE] {
    def warnTrailing(ctx: SeqBlockContext): List[RESETSTYLE] = {
      val statements = ctx.statement.toList
      for ((a, b) <- statements zip statements.tail; if !a.resetClause.isEmpty)
        yield RESETSTYLE(b.loc)
    }

    override def visitSeqBlock(ctx: SeqBlockContext) = {
      if (!ctx.statement.isEmpty) {
        visit(ctx.statement.head) ::: ctx.statement.toList.tail.flatMap(WarnIn(_)) ::: warnTrailing(ctx)
      } else {
        Nil
      }
    }

    override def visitIfStatement(ctx: IfStatementContext) = {
      val elifWarnings = for (elifCond <- ctx.elifConds; if (!CollectResetNames(elifCond).isEmpty))
        yield RESETSTYLE(elifCond.loc)

      WarnIn(ctx.ifBlock) ::: elifWarnings.toList ::: WarnIn(ctx.elifBlocks) ::: WarnIn(ctx.elseBlock)
    }

    override def visitStmtCase(ctx: StmtCaseContext) = WarnIn(ctx)
    override def visitStmtLoop(ctx: StmtLoopContext) = WarnIn(ctx)
  }

  object RESETSTYLESourceAnalyserVisitor extends WarningsSourceAnalyserVisitor[RESETSTYLE] {
    override def visitAlwaysEvent(ctx: AlwaysEventContext) = {
      AlwaysStatementVisitor(ctx.statement)
    }
  }

  def apply(source: Source) = {
    RESETSTYLESourceAnalyserVisitor(source)
  }
}

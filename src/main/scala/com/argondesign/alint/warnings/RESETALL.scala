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
import com.argondesign.alint.Visitor
import org.antlr.v4.runtime.ParserRuleContext
import com.argondesign.alint.SignalNames

final case class RESETALL(val loc: Loc, name: String, subtype: Int) extends SourceWarning {
  val message = subtype match {
    case 0 => s"Signal '$name' not assigned in reset clasue"
    case 1 => s"Signal '$name' only assigned in reset clasue"
  }
}

object RESETALL extends SourceAnalyser[List[RESETALL]] {
  object NORESETSourceAnalyserVisitor extends WarningsSourceAnalyserVisitor[RESETALL] {
    override def visitAlwaysEvent(ctx: AlwaysEventContext) = {
      ctx.statement.resetClause match {
        case Some(resetClauseCtx) => {
          object GetNBATargets extends Visitor[List[ParserRuleContext]](Nil, _ ::: _) {
            object GetTargetIdentifier extends Visitor[List[ParserRuleContext]](Nil, _ ::: _) {
              override def visitVarLValueSimple(ctx: VarLValueSimpleContext) = List(ctx.hierId)
              override def visitVarLValueSlice(ctx: VarLValueSliceContext) = List(ctx.hierId)
              override def visitVarLValueIndex(ctx: VarLValueIndexContext) = List(ctx.hierId)
              override def visitVarLValueIndexSlice(ctx: VarLValueIndexSliceContext) = List(ctx.hierId)
            }
            override def visitNonblockingAssignment(ctx: NonblockingAssignmentContext) = {
              GetTargetIdentifier(ctx.variableLvalue)
            }
          }

          val allTargets = GetNBATargets(ctx.statement)
          val resetTargets = allTargets filter (_ -<- resetClauseCtx)
          val otherTargets = allTargets filter (_ !<- resetClauseCtx)
          val resetNames = resetTargets map (_.text)
          val otherNames = otherTargets map (_.text)

          val nonResetWarnigns =
            for (target <- otherTargets; if !resetNames.contains(target.text))
              yield RESETALL(target.loc, target.text, 0)

          val onlyResetWarnigns =
            for (target <- resetTargets; if !otherNames.contains(target.text))
              yield RESETALL(target.loc, target.text, 1)

          nonResetWarnigns ::: onlyResetWarnigns
        }
        case None => Nil
      }
    }
  }

  def apply(source: Source) = NORESETSourceAnalyserVisitor(source)
}

////////////////////////////////////////////////////////////////////////////////
// Argon Design Ltd.
// Copyright (c) 2016-2018 Argon Design Ltd. All rights reserved.
//
// This file is covered by the BSD (with attribution) license.
// See the LICENSE file for the precise wording of the license.
//
// Module : Argon Verilog Tools
// Author : Geza Lore
//
// DESCRIPTION:
//
////////////////////////////////////////////////////////////////////////////////

package com.argondesign.vtools.warnings

import scala.collection.mutable.Stack

import org.antlr.v4.runtime.ParserRuleContext

import com.argondesign.vtools.Loc
import com.argondesign.vtools.Source
import com.argondesign.vtools.SourceAnalyser
import com.argondesign.vtools.SourceWarning
import com.argondesign.vtools.Visitor

final case class GENUNIQ(val loc: Loc, name: String) extends SourceWarning {
  val message = s"Generate block names should be unique: $name"
}

object GENUNIQ extends SourceAnalyser[List[GENUNIQ]] {
  def apply(source: Source) = {
    val countMap = source.ctxNames.values.groupBy(identity) map (p => (p._1, p._2.size))

    object Warn extends Visitor[List[GENUNIQ]](Nil, _ ::: _) {
      override def visitGenerateBlockWithBeginEnd(ctx: GenerateBlockWithBeginEndContext) = {
        val name = source.ctxNames(ctx)
        if (countMap(name) > 1) {
          GENUNIQ(ctx.loc, name) :: visitChildren(ctx)
        } else {
          visitChildren(ctx)
        }
      }
    }

    Warn(source.parseTree)
  }
}

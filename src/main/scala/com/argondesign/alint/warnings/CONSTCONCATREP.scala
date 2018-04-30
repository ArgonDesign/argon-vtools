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

import com.argondesign.vtools.Loc
import com.argondesign.vtools.Source
import com.argondesign.vtools.SourceAnalyser
import com.argondesign.vtools.SourceWarning

final case class CONSTCONCATREP(val loc: Loc, symbol: String) extends SourceWarning {
  // DC doesn't like it
  val message = s"System function '$symbol' called in repetition multiplier of constant concatenation"
}

object CONSTCONCATREP extends SourceAnalyser[List[CONSTCONCATREP]] {
  object CONSTCONCATREPSourceAnalyserVisitor extends WarningsSourceAnalyserVisitor[CONSTCONCATREP] {
    object WarnIn extends WarningsSourceAnalyserVisitor[CONSTCONCATREP] {
      override def visitConstSysFuncCall(ctx: ConstSysFuncCallContext) = {
        CONSTCONCATREP(ctx.loc, ctx.SYSID.text) :: visitChildren(ctx)
      }
    }

    override def visitConstMultipleConcat(ctx: ConstMultipleConcatContext) = {
      WarnIn(ctx.rep) ::: this(ctx.terms)
    }
  }

  def apply(source: Source) = CONSTCONCATREPSourceAnalyserVisitor(source)
}

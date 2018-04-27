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

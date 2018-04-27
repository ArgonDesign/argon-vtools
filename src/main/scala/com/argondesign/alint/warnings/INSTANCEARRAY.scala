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
import org.antlr.v4.runtime.Token

final case class INSTANCEARRAY(val loc: Loc) extends SourceWarning {
  val message = "Do not use instance arrays"
}

object INSTANCEARRAY extends SourceAnalyser[List[INSTANCEARRAY]] {
  object INSTANCEARRAYSourceAnalyserVisitor extends WarningsSourceAnalyserVisitor[INSTANCEARRAY] {
    override def visitNameOfModuleInstance(ctx: NameOfModuleInstanceContext) = {
      if (ctx.vrange != null) {
        List(INSTANCEARRAY(ctx.loc))
      } else {
        Nil
      }
    }
  }

  def apply(source: Source) = INSTANCEARRAYSourceAnalyserVisitor(source)
}

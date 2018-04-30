////////////////////////////////////////////////////////////////////////////////
// Argon Design Ltd. Project P9000 Argon
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

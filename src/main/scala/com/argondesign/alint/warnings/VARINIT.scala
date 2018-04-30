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

final case class VARINIT(val loc: Loc, name: String) extends SourceWarning {
  val message = s"Variable declaration with initialisation: '$name'"
}

object VARINIT extends SourceAnalyser[List[VARINIT]] {
  object WarnIn extends WarningsSourceAnalyserVisitor[VARINIT] {
    override def visitVariableTypeInitialised(ctx: VariableTypeInitialisedContext) = {
      List(VARINIT(ctx.loc, ctx.IDENTIFIER))
    }
  }

  object VARINITSourceAnalyserVisitor extends WarningsSourceAnalyserVisitor[VARINIT] {
    override def visitRegDeclaration(ctx: RegDeclarationContext) = {
      WarnIn(ctx.listOfVariableIdentifiers)
    }
  }

  def apply(source: Source) = VARINITSourceAnalyserVisitor(source)
}

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

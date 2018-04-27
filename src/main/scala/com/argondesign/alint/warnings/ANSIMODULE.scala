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

final case class ANSIMODULE(val loc: Loc, name: String) extends SourceWarning {
  val message = s"Module '$name': Use ANSI style module declarations"
}

object ANSIMODULE extends SourceAnalyser[List[ANSIMODULE]] {
  object ANSIMODULESourceAnalyserVisitor extends WarningsSourceAnalyserVisitor[ANSIMODULE] {
    override def visitModuleDeclarationNonAnsi(ctx: ModuleDeclarationNonAnsiContext) = {
      List(ANSIMODULE(ctx.IDENTIFIER.loc, ctx.IDENTIFIER))
    }
  }

  def apply(source: Source) = ANSIMODULESourceAnalyserVisitor(source)
}

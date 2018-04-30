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

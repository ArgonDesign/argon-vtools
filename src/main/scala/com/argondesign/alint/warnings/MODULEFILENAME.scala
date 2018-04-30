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

final case class MODULEFILENAME(val loc: Loc, name: String) extends SourceWarning {
  val message = s"Name of declared module '$name' does not match source file name"
}

object MODULEFILENAME extends SourceAnalyser[List[MODULEFILENAME]] {
  def apply(source: Source) = {
    object MODULEFILENAMESourceAnalyserVisitor extends WarningsSourceAnalyserVisitor[MODULEFILENAME] {
      def check(id: Token) = {
        val name = source.name takeWhile { _ != '.' }
        if (name != id.text) {
          List(MODULEFILENAME(id.loc, id.text))
        } else Nil
      }
      override def visitModuleDeclarationNonAnsi(ctx: ModuleDeclarationNonAnsiContext) = check(ctx.IDENTIFIER)
      override def visitModuleDeclarationAnsi(ctx: ModuleDeclarationAnsiContext) = check(ctx.IDENTIFIER)
    }
    MODULEFILENAMESourceAnalyserVisitor(source)
  }
}

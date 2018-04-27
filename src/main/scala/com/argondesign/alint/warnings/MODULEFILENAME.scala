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

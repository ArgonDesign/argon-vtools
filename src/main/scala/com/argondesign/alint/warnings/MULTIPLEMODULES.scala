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

import org.antlr.v4.runtime.Token

import com.argondesign.vtools.Loc
import com.argondesign.vtools.Source
import com.argondesign.vtools.SourceAnalyser
import com.argondesign.vtools.SourceWarning
import com.argondesign.vtools.Visitor
import com.argondesign.vtools.Antlr4Conversions._

final case class MULTIPLEMODULES(val loc: Loc, name: String) extends SourceWarning {
  val message = s"File declares multple modules: '$name'"
}

object MULTIPLEMODULES extends SourceAnalyser[List[MULTIPLEMODULES]] {
  object ModuleNameIds extends Visitor[List[Token]](Nil, _ ::: _) {
    override def visitModuleDeclarationAnsi(ctx: ModuleDeclarationAnsiContext) = List(ctx.IDENTIFIER)
    override def visitModuleDeclarationNonAnsi(ctx: ModuleDeclarationNonAnsiContext) = List(ctx.IDENTIFIER)
  }

  def apply(source: Source) = {
    val moduleNameIds = ModuleNameIds(source)
    if (moduleNameIds.size >= 2) {
      for (id <- moduleNameIds) yield MULTIPLEMODULES(id.loc, id.text)
    } else {
      Nil
    }
  }
}

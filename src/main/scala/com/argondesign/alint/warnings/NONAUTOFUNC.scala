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

final case class NONAUTOFUNC(val loc: Loc, name: String) extends SourceWarning {
  val message = s"Non-automatic (static) functions should not be used: '$name'"
}

object NONAUTOFUNC extends SourceAnalyser[List[NONAUTOFUNC]] {
  object NONAUTOFUNCSourceAnalyserVisitor extends WarningsSourceAnalyserVisitor[NONAUTOFUNC] {
    override def visitFunctionDeclaration(ctx: FunctionDeclarationContext) = {
      if (ctx.AUTO == null) List(NONAUTOFUNC(ctx.loc, ctx.IDENTIFIER)) else Nil
    }
  }

  def apply(source: Source) = NONAUTOFUNCSourceAnalyserVisitor(source)
}

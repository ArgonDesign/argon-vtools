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

final case class ATSTAR(val loc: Loc) extends SourceWarning {
  val message = "Use '@*' instead of '@(*)'"
}

object ATSTAR extends SourceAnalyser[List[ATSTAR]] {
  object ATSTARSourceAnalyserVisitor extends WarningsSourceAnalyserVisitor[ATSTAR] {
    override def visitAlwaysAtStar(ctx: AlwaysAtStarContext) = {
      if (ctx.atStar.text == "@(*)") List(ATSTAR(ctx.loc)) else Nil
    }
  }

  def apply(source: Source) = ATSTARSourceAnalyserVisitor(source)
}

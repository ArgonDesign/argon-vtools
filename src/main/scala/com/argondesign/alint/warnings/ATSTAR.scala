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

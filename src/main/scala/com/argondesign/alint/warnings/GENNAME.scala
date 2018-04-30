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

final case class GENNAME(val loc: Loc) extends SourceWarning {
  val message = "Unnamed generate block"
}

object GENNAME extends SourceAnalyser[List[GENNAME]] {
  object GENNAMESourceAnalyserVisitor extends WarningsSourceAnalyserVisitor[GENNAME] {
    override def visitGenerateBlockWithBeginEnd(ctx: GenerateBlockWithBeginEndContext) = {
      if (ctx.IDENTIFIER == null) {
        GENNAME(ctx.loc) :: visitChildren(ctx)
      } else {
        visitChildren(ctx)
      }
    }
  }

  def apply(source: Source) = GENNAMESourceAnalyserVisitor(source)
}

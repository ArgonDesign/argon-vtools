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

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

final case class GENBEGIN(val loc: Loc) extends SourceWarning {
  val message = "Generate block without begin end"
}

object GENBEGIN extends SourceAnalyser[List[GENBEGIN]] {
  object GENBEGINSourceAnalyserVisitor extends WarningsSourceAnalyserVisitor[GENBEGIN] {
    override def visitGenerateBlockWithoutBeginEnd(ctx: GenerateBlockWithoutBeginEndContext) = {
      GENBEGIN(ctx.loc) :: visitChildren(ctx)
    }
  }

  def apply(source: Source) = GENBEGINSourceAnalyserVisitor(source)
}

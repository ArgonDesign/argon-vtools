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

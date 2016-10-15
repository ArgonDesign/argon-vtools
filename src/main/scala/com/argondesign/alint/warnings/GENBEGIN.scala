package com.argondesign.alint.warnings

import com.argondesign.alint.Loc

final case class GENBEGIN(val loc: Loc) extends Warning {
  val text = "Generate block without begin end"
}

object GENBEGIN {
  implicit object GENBEGINSourceAnalysisVisitor extends WarningsSourceAnalysisVisitor[GENBEGIN] {
    override def visitGenerateBlockWithoutBeginEnd(ctx: GenerateBlockWithoutBeginEndContext) = {
      GENBEGIN(ctx.loc) :: visitChildren(ctx)
    }
  }
}

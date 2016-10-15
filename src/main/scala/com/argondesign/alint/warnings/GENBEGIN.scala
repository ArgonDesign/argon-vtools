package com.argondesign.alint.warnings

import com.argondesign.alint.Loc
import com.argondesign.alint.SourceWarning

final case class GENBEGIN(val loc: Loc) extends SourceWarning {
  val message = "Generate block without begin end"
}

object GENBEGIN {
  implicit object GENBEGINSourceAnalysisVisitor extends WarningsSourceAnalysisVisitor[GENBEGIN] {
    override def visitGenerateBlockWithoutBeginEnd(ctx: GenerateBlockWithoutBeginEndContext) = {
      GENBEGIN(ctx.loc) :: visitChildren(ctx)
    }
  }
}

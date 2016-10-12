package com.argondesign.alint.warnings

import com.argondesign.alint.Loc

final case class GENBEGIN(val loc: Loc) extends Warning {
  val text = s"Generate block without begin end"
}

object GENBEGIN {
  implicit object GENBEGINSourceAnalysisVisitor extends WarningsSourceAnalysisVisitor[GENBEGIN] {
    import com.argondesign.alint.antlr4.VParser._

    override def visitGenerateBlockWithoutBeginEnd(ctx: GenerateBlockWithoutBeginEndContext) = {
      GENBEGIN(ctx.loc) :: visitChildren(ctx)
    }
  }
}

package com.argondesign.alint.warnings

import com.argondesign.alint.Loc
import com.argondesign.alint.SourceWarning

final case class GENNAME(val loc: Loc) extends SourceWarning {
  val message = "Unnamed generate block"
}

object GENNAME {
  implicit object GENNAMESourceAnalysisVisitor extends WarningsSourceAnalysisVisitor[GENNAME] {
    override def visitGenerateBlockWithBeginEnd(ctx: GenerateBlockWithBeginEndContext) = {
      if (ctx.IDENTIFIER == null) {
        GENNAME(ctx.loc) :: visitChildren(ctx)
      } else {
        visitChildren(ctx)
      }
    }
  }
}

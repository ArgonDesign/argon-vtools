package com.argondesign.alint.warnings

import com.argondesign.alint.Loc

final case class GENNAME(val loc: Loc) extends Warning {
  val text = s"Unnamed generate block"
}

object GENNAME {
  implicit object GENNAMESourceAnalysisVisitor extends WarningsSourceAnalysisVisitor[GENNAME] {
    import com.argondesign.alint.antlr4.VParser._

    override def visitGenerateBlockWithBeginEnd(ctx: GenerateBlockWithBeginEndContext) = {
      if (ctx.IDENTIFIER eq null) {
        GENNAME(ctx.loc) :: visitChildren(ctx)
      } else {
        visitChildren(ctx)
      }
    }
  }
}

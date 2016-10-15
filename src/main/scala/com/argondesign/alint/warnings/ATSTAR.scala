package com.argondesign.alint.warnings

import com.argondesign.alint.Loc
import com.argondesign.alint.Visitor
import com.argondesign.alint.SourceWarning

final case class ATSTAR(val loc: Loc) extends SourceWarning {
  val message = "Both blocking and non-blocking assignments used in the same always block"
}

object ATSTAR {
  implicit object ATSTARSourceAnalysisVisitor extends WarningsSourceAnalysisVisitor[ATSTAR] {
    override def visitAlwaysAtStar(ctx: AlwaysAtStarContext) = {
      if (ctx.atStar.text == "@(*)") List(ATSTAR(ctx.loc)) else Nil
    }
  }
}

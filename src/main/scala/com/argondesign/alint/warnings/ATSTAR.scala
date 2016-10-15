package com.argondesign.alint.warnings

import com.argondesign.alint.Loc
import com.argondesign.alint.Source
import com.argondesign.alint.SourceAnalyser
import com.argondesign.alint.SourceWarning

final case class ATSTAR(val loc: Loc) extends SourceWarning {
  val message = "Both blocking and non-blocking assignments used in the same always block"
}

object ATSTAR extends SourceAnalyser[List[ATSTAR]] {
  object ATSTARSourceAnalysisVisitor extends WarningsSourceAnalyserVisitor[ATSTAR] {
    override def visitAlwaysAtStar(ctx: AlwaysAtStarContext) = {
      if (ctx.atStar.text == "@(*)") List(ATSTAR(ctx.loc)) else Nil
    }
  }

  def apply(source: Source) = ATSTARSourceAnalysisVisitor(source)
}

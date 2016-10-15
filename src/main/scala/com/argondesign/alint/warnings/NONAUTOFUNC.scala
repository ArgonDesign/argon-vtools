package com.argondesign.alint.warnings

import com.argondesign.alint.Loc
import com.argondesign.alint.Source
import com.argondesign.alint.SourceAnalyser
import com.argondesign.alint.SourceWarning

final case class NONAUTOFUNC(val loc: Loc, name: String) extends SourceWarning {
  val message = s"Non-automatic (static) functions should not be used: '$name'"
}

object NONAUTOFUNC extends SourceAnalyser[List[NONAUTOFUNC]] {
  object NONAUTOFUNCSourceAnalysisVisitor extends WarningsSourceAnalyserVisitor[NONAUTOFUNC] {
    override def visitFunctionDeclaration(ctx: FunctionDeclarationContext) = {
      if (ctx.AUTO == null) List(NONAUTOFUNC(ctx.loc, ctx.IDENTIFIER)) else Nil
    }
  }

  def apply(source: Source) = NONAUTOFUNCSourceAnalysisVisitor(source)
}

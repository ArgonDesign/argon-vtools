package com.argondesign.alint.warnings

import com.argondesign.alint.Loc
import com.argondesign.alint.Source
import com.argondesign.alint.SourceAnalyser
import com.argondesign.alint.SourceWarning

final case class VARINIT(val loc: Loc, name: String) extends SourceWarning {
  val message = s"Variable declaration with initialisation: '$name'"
}

object VARINIT extends SourceAnalyser[List[VARINIT]] {
  object WarnIn extends WarningsSourceAnalyserVisitor[VARINIT] {
    override def visitVariableTypeInitialised(ctx: VariableTypeInitialisedContext) = {
      List(VARINIT(ctx.loc, ctx.IDENTIFIER))
    }
  }

  object VARINITSourceAnalyserVisitor extends WarningsSourceAnalyserVisitor[VARINIT] {
    override def visitRegDeclaration(ctx: RegDeclarationContext) = {
      WarnIn(ctx.listOfVariableIdentifiers)
    }
  }

  def apply(source: Source) = VARINITSourceAnalyserVisitor(source)
}

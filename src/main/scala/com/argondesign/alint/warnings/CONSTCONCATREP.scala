package com.argondesign.alint.warnings

import com.argondesign.alint.Loc

final case class CONSTCONCATREP(val loc: Loc, symbol: String) extends Warning {
  // DC doesn't like it
  val text = s"System function '$symbol' called in repetition multiplier of constant concatenation"
}

object CONSTCONCATREP {
  implicit object CONSTCONCATREPSourceAnalysisVisitor extends WarningsSourceAnalysisVisitor[CONSTCONCATREP] {
    object WarnIn extends WarningsSourceAnalysisVisitor[CONSTCONCATREP] {
      override def visitConstantSystemFunctionCall(ctx: ConstantSystemFunctionCallContext) = {
        CONSTCONCATREP(ctx.loc, ctx.SYSID.text) :: visitChildren(ctx)
      }
    }

    override def visitConstantMultipleConcatenation(ctx: ConstantMultipleConcatenationContext) = {
      WarnIn(ctx.constantExpression) ::: visitChildren(ctx.constantConcatenation)
    }
  }
}

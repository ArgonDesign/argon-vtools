package com.argondesign.alint.warnings

import com.argondesign.alint.Loc
import com.argondesign.alint.Source
import com.argondesign.alint.SourceAnalyser
import com.argondesign.alint.SourceWarning

final case class CONSTCONCATREP(val loc: Loc, symbol: String) extends SourceWarning {
  // DC doesn't like it
  val message = s"System function '$symbol' called in repetition multiplier of constant concatenation"
}

object CONSTCONCATREP extends SourceAnalyser[List[CONSTCONCATREP]] {
  object CONSTCONCATREPSourceAnalysisVisitor extends WarningsSourceAnalysisVisitor[CONSTCONCATREP] {
    object WarnIn extends WarningsSourceAnalysisVisitor[CONSTCONCATREP] {
      override def visitConstantSystemFunctionCall(ctx: ConstantSystemFunctionCallContext) = {
        CONSTCONCATREP(ctx.loc, ctx.SYSID.text) :: visitChildren(ctx)
      }
    }

    override def visitConstantMultipleConcatenation(ctx: ConstantMultipleConcatenationContext) = {
      WarnIn(ctx.constantExpression) ::: visitChildren(ctx.constantConcatenation)
    }
  }

  def apply(source: Source) = CONSTCONCATREPSourceAnalysisVisitor(source)
}

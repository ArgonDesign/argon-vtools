package com.argondesign.alint.warnings

import com.argondesign.alint.Loc
import com.argondesign.alint.Source
import com.argondesign.alint.SourceAnalyser
import com.argondesign.alint.SourceWarning

final case class GENBEGIN(val loc: Loc) extends SourceWarning {
  val message = "Generate block without begin end"
}

object GENBEGIN extends SourceAnalyser[List[GENBEGIN]] {
  object GENBEGINSourceAnalyserVisitor extends WarningsSourceAnalyserVisitor[GENBEGIN] {
    override def visitGenerateBlockWithoutBeginEnd(ctx: GenerateBlockWithoutBeginEndContext) = {
      GENBEGIN(ctx.loc) :: visitChildren(ctx)
    }
  }

  def apply(source: Source) = GENBEGINSourceAnalyserVisitor(source)
}

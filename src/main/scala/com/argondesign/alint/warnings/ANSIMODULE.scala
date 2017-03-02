package com.argondesign.alint.warnings

import com.argondesign.alint.Loc
import com.argondesign.alint.Source
import com.argondesign.alint.SourceAnalyser
import com.argondesign.alint.SourceWarning

final case class ANSIMODULE(val loc: Loc, name: String) extends SourceWarning {
  val message = s"Module '$name': Use ANSI style module declarations"
}

object ANSIMODULE extends SourceAnalyser[List[ANSIMODULE]] {
  object ANSIMODULESourceAnalyserVisitor extends WarningsSourceAnalyserVisitor[ANSIMODULE] {
    override def visitModuleDeclarationNonAnsi(ctx: ModuleDeclarationNonAnsiContext) = {
      List(ANSIMODULE(ctx.IDENTIFIER.loc, ctx.IDENTIFIER))
    }
  }

  def apply(source: Source) = ANSIMODULESourceAnalyserVisitor(source)
}

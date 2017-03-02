package com.argondesign.alint.warnings

import org.antlr.v4.runtime.Token

import com.argondesign.alint.Loc
import com.argondesign.alint.Source
import com.argondesign.alint.SourceAnalyser
import com.argondesign.alint.SourceWarning
import com.argondesign.alint.Visitor
import com.argondesign.alint.Antlr4Conversions._

final case class MULTIPLEMODULES(val loc: Loc, name: String) extends SourceWarning {
  val message = s"File declares multple modules: '$name'"
}

object MULTIPLEMODULES extends SourceAnalyser[List[MULTIPLEMODULES]] {
  object ModuleNameIds extends Visitor[List[Token]](Nil, _ ::: _) {
    override def visitModuleDeclarationAnsi(ctx: ModuleDeclarationAnsiContext) = List(ctx.IDENTIFIER)
    override def visitModuleDeclarationNonAnsi(ctx: ModuleDeclarationNonAnsiContext) = List(ctx.IDENTIFIER)
  }

  def apply(source: Source) = {
    val moduleNameIds = ModuleNameIds(source)
    if (moduleNameIds.size >= 2) {
      for (id <- moduleNameIds) yield MULTIPLEMODULES(id.loc, id.text)
    } else {
      Nil
    }
  }
}

package com.argondesign.alint.warnings

import scala.collection.mutable.Stack

import org.antlr.v4.runtime.ParserRuleContext

import com.argondesign.alint.Loc
import com.argondesign.alint.Source
import com.argondesign.alint.SourceAnalyser
import com.argondesign.alint.SourceWarning
import com.argondesign.alint.Visitor

final case class GENUNIQ(val loc: Loc, name: String) extends SourceWarning {
  val message = s"Generate block names should be unique: $name"
}

object GENUNIQ extends SourceAnalyser[List[GENUNIQ]] {
  def apply(source: Source) = {
    val countMap = source.ctxNames.values.groupBy(identity) map (p => (p._1, p._2.size))

    object Warn extends Visitor[List[GENUNIQ]](Nil, _ ::: _) {
      override def visitGenerateBlockWithBeginEnd(ctx: GenerateBlockWithBeginEndContext) = {
        val name = source.ctxNames(ctx)
        if (countMap(name) > 1) {
          GENUNIQ(ctx.loc, name) :: visitChildren(ctx)
        } else {
          visitChildren(ctx)
        }
      }
    }

    Warn(source.parseTree)
  }
}

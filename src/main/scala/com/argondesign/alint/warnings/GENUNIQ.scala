package com.argondesign.alint.warnings

import com.argondesign.alint.Loc
import com.argondesign.alint.SourceAnalysis
import com.argondesign.alint.Source
import com.argondesign.alint.Visitor
import scala.collection.mutable.Stack
import org.antlr.v4.runtime.ParserRuleContext

final case class GENUNIQ(val loc: Loc, name: String) extends Warning {
  val text = s"Generate block names should be unique: $name"
}

object GENUNIQ {
  implicit object GENUNIQSourceAnalysis extends SourceAnalysis[List[GENUNIQ]] {

    override def apply(source: Source) = {
      object CollectGenNames extends Visitor[Map[ParserRuleContext, String]](Map(), _ ++ _) {
        val nameStack = Stack[String]()
        val countStack = Stack[Int]()
        var count = 0

        override def visitGenerateBlockWithBeginEnd(ctx: GenerateBlockWithBeginEndContext) = {
          if (ctx.IDENTIFIER != null) {
            nameStack.push(ctx.IDENTIFIER)
          } else {
            nameStack.push("genblk" + count); count += 1;
          }
          countStack push count; count = 0

          visitChildren(ctx) + (ctx -> nameStack.reverse.mkString("."))
        } restoring {
          count = countStack.pop()
          nameStack.pop()
        }
      }

      val nameMap = CollectGenNames(source.parseTree)
      val countMap = nameMap.values.groupBy(identity) map (p => (p._1, p._2.size))

      object Warn extends Visitor[List[GENUNIQ]](Nil, _ ::: _) {
        override def visitGenerateBlockWithBeginEnd(ctx: GenerateBlockWithBeginEndContext) = {
          val name = nameMap(ctx)
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
}

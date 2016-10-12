package com.argondesign.alint.warnings

import scala.collection.mutable.ListBuffer

import com.argondesign.alint.Visitor
import com.argondesign.alint.Loc

sealed abstract class DNETTYPE extends Warning {}

final case class DNETTYPE1A(val loc: Loc) extends DNETTYPE {
  val text = "No '`default_nettype none' directive at beginning of file"
}

final case class DNETTYPE1B(val loc: Loc) extends DNETTYPE {
  val text = "No '`default_nettype wire' directive at end of file"
}

final case class DNETTYPE2A(val loc: Loc) extends DNETTYPE {
  val text = "More than 1 `default_nettype directive at beginning of file"
}

final case class DNETTYPE2B(val loc: Loc) extends DNETTYPE {
  val text = "More than 1 `default_nettype directive at end of file"
}

final case class DNETTYPE3A(val loc: Loc) extends DNETTYPE {
  val text = "`default_nettype directive is not first line at beginning of file"
}

final case class DNETTYPE3B(val loc: Loc) extends DNETTYPE {
  val text = "`default_nettype directive is not last line at end of file"
}

final case class DNETTYPE4A(val loc: Loc) extends DNETTYPE {
  val text = "`default_nettype dirctive with non 'none' type at beginning of file"
}

final case class DNETTYPE4B(val loc: Loc) extends DNETTYPE {
  val text = "`default_nettype directive with non 'wire' type at end of file"
}

object DNETTYPE {
  implicit object DNETTYPESourceAnalysisVisitor extends WarningsSourceAnalysisVisitor[DNETTYPE] {
    import com.argondesign.alint.antlr4.VParser._

    object CountDefaultNetType extends Visitor[Int](0, _ + _) {
      override def visitDefaultNettypeDirective(ctx: DefaultNettypeDirectiveContext) = 1
    }

    object GetDefaultNettype extends Visitor[String]("", (a, b) => b) {
      override def visitDefaultNettypeDirective(ctx: DefaultNettypeDirectiveContext) = ctx.IDENTIFIER.text
    }

    override def visitSourceText(ctx: SourceTextContext) = {
      var res = ListBuffer[DNETTYPE]()

      val headerCount = CountDefaultNetType(ctx.headerDirectives)

      if (headerCount == 0) {
        res += DNETTYPE1A(ctx.start.loc)
      } else if (headerCount > 1) {
        res += DNETTYPE2A(ctx.start.loc)
      } else if (CountDefaultNetType(ctx.headerDirectives.head) == 0) {
        res += DNETTYPE3A(ctx.start.loc)
      } else if (GetDefaultNettype(ctx.headerDirectives.head) != "none") {
        res += DNETTYPE4A(ctx.start.loc)
      }

      val footerCount = CountDefaultNetType(ctx.footerDirectives)

      if (footerCount == 0) {
        res += DNETTYPE1B(ctx.start.loc)
      } else if (footerCount > 1) {
        res += DNETTYPE2B(ctx.start.loc)
      } else if (CountDefaultNetType(ctx.footerDirectives.reverse.last) == 0) {
        res += DNETTYPE3B(ctx.start.loc)
      } else if (GetDefaultNettype(ctx.headerDirectives.reverse.last) != "wire") {
        res += DNETTYPE4B(ctx.start.loc)
      }

      res.toList
    }
  }
}

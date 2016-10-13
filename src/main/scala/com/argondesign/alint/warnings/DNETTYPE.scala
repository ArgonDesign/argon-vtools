package com.argondesign.alint.warnings

import scala.collection.mutable.ListBuffer

import com.argondesign.alint.Visitor
import com.argondesign.alint.Loc

final case class DNETTYPE(val loc: Loc, subtype: Int) extends Warning {
  val text = subtype match {
    case 0 => "No '`default_nettype none' directive at beginning of file"
    case 1 => "No '`default_nettype wire' directive at end of file"
    case 2 => "More than 1 `default_nettype directive at beginning of file"
    case 3 => "More than 1 `default_nettype directive at end of file"
    case 4 => "`default_nettype directive is not first line at beginning of file"
    case 5 => "`default_nettype directive is not last line at end of file"
    case 6 => "`default_nettype directive with non 'none' type at beginning of file"
    case 7 => "`default_nettype directive with non 'wire' type at end of file"
  }
}

object DNETTYPE {
  implicit object DNETTYPESourceAnalysisVisitor extends WarningsSourceAnalysisVisitor[DNETTYPE] {
    import com.argondesign.alint.antlr4.VParser._

    object CountDNT extends Visitor[Int](0, _ + _) {
      override def visitDefaultNettypeDirective(ctx: DefaultNettypeDirectiveContext) = 1
    }

    object GetDNT extends Visitor[String]("", (a, b) => b) {
      override def visitDefaultNettypeDirective(ctx: DefaultNettypeDirectiveContext) =
        if (ctx.IDENTIFIER != null) ctx.IDENTIFIER.text else ctx.netType.text
    }

    object FirstDNTLoc extends Visitor[Loc](null, (a, b) => if (a == null) b else a) {
      override def visitDefaultNettypeDirective(ctx: DefaultNettypeDirectiveContext) = ctx.loc
    }

    override def visitSourceText(ctx: SourceTextContext) = {
      var res = ListBuffer[DNETTYPE]()

      val headerCount = CountDNT(ctx.headerDirectives)

      if (headerCount == 0) {
        res += DNETTYPE(ctx.loc, 0)
      } else if (headerCount > 1) {
        res += DNETTYPE(ctx.loc, 2)
      } else if (CountDNT(ctx.headerDirectives.head) == 0) {
        res += DNETTYPE(FirstDNTLoc(ctx.headerDirectives), 4)
      } else if (GetDNT(ctx.headerDirectives.head) != "none") {
        res += DNETTYPE(ctx.loc, 6)
      }

      val footerCount = CountDNT(ctx.footerDirectives)

      if (footerCount == 0) {
        res += DNETTYPE(ctx.stop.loc, 1)
      } else if (footerCount > 1) {
        res += DNETTYPE(FirstDNTLoc(ctx.footerDirectives), 3)
      } else if (CountDNT(ctx.footerDirectives.last) == 0) {
        res += DNETTYPE(FirstDNTLoc(ctx.footerDirectives), 5)
      } else if (GetDNT(ctx.footerDirectives.last) != "wire") {
        res += DNETTYPE(ctx.footerDirectives.last.loc, 7)
      }

      res.toList
    }
  }
}

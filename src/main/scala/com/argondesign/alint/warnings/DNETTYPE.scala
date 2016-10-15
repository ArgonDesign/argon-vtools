package com.argondesign.alint.warnings

import scala.collection.mutable.ListBuffer

import com.argondesign.alint.Loc
import com.argondesign.alint.Source
import com.argondesign.alint.SourceAnalyser
import com.argondesign.alint.SourceWarning
import com.argondesign.alint.Visitor

final case class DNETTYPE(val loc: Loc, subtype: Int) extends SourceWarning {
  val message = subtype match {
    case 0 => "No '`default_nettype none' directive at beginning of file"
    case 1 => "No '`default_nettype wire' directive at end of file"
    case 2 => "More than 1 `default_nettype directive at beginning of file"
    case 3 => "More than 1 `default_nettype directive at end of file"
    case 4 => "`default_nettype directive is not last directive at beginning of file"
    case 5 => "`default_nettype directive is not first directive at end of file"
    case 6 => "`default_nettype directive with non 'none' type at beginning of file"
    case 7 => "`default_nettype directive with non 'wire' type at end of file"
  }
}

object DNETTYPE extends SourceAnalyser[List[DNETTYPE]] {
  implicit object DNETTYPESourceAnalysisVisitor extends WarningsSourceAnalyserVisitor[DNETTYPE] {
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
      } else if (CountDNT(ctx.headerDirectives.last) == 0) {
        res += DNETTYPE(FirstDNTLoc(ctx.headerDirectives), 4)
      } else if (GetDNT(ctx.headerDirectives.last) != "none") {
        res += DNETTYPE(ctx.loc, 6)
      }

      val footerCount = CountDNT(ctx.footerDirectives)

      if (footerCount == 0) {
        res += DNETTYPE(ctx.stop.loc, 1)
      } else if (footerCount > 1) {
        res += DNETTYPE(FirstDNTLoc(ctx.footerDirectives), 3)
      } else if (CountDNT(ctx.footerDirectives.head) == 0) {
        res += DNETTYPE(FirstDNTLoc(ctx.footerDirectives), 5)
      } else if (GetDNT(ctx.footerDirectives.head) != "wire") {
        res += DNETTYPE(ctx.footerDirectives.head.loc, 7)
      }

      res.toList
    }
  }

  def apply(source: Source) = DNETTYPESourceAnalysisVisitor(source)
}

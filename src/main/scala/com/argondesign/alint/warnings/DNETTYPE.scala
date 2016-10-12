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
  val text = "`default_nettype directive with non 'none' type at beginning of file"
}

final case class DNETTYPE4B(val loc: Loc) extends DNETTYPE {
  val text = "`default_nettype directive with non 'wire' type at end of file"
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
        res += DNETTYPE1A(ctx.loc)
      } else if (headerCount > 1) {
        res += DNETTYPE2A(ctx.loc)
      } else if (CountDNT(ctx.headerDirectives.head) == 0) {
        res += DNETTYPE3A(FirstDNTLoc(ctx.headerDirectives))
      } else if (GetDNT(ctx.headerDirectives.head) != "none") {
        res += DNETTYPE4A(ctx.loc)
      }

      val footerCount = CountDNT(ctx.footerDirectives)

      if (footerCount == 0) {
        res += DNETTYPE1B(ctx.stop.loc)
      } else if (footerCount > 1) {
        res += DNETTYPE2B(FirstDNTLoc(ctx.footerDirectives))
      } else if (CountDNT(ctx.footerDirectives.last) == 0) {
        res += DNETTYPE3B(FirstDNTLoc(ctx.footerDirectives))
      } else if (GetDNT(ctx.footerDirectives.last) != "wire") {
        res += DNETTYPE4B(ctx.footerDirectives.last.loc)
      }

      res.toList
    }
  }
}

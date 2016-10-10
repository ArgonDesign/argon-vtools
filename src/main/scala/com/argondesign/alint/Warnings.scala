package com.argondesign.alint

object Warnings {
  abstract sealed class Warning {
    val loc: Loc
    val text: String
    override def toString = loc + " Warning : " + this.getClass.getSimpleName + " - " + text
  }

  final case class CCREPSYS(loc: Loc, symbol: String) extends Warning {
    // DC doesn't like it
    val text = s"System function '$symbol' called in repetition multiplier of constant concatenation"
  }

  final case class BLKSEQ(loc: Loc) extends Warning {
    val text = s"Both blocking and non-blocking assignments used in the same always block"
  }

  final case class GENNAME(loc: Loc) extends Warning {
    val text = s"Unnamed generate block"
  }
}

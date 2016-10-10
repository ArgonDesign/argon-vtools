package com.argondesign.alint

object Warnings {
  abstract sealed class Warning {
    val loc: Loc
    val text: String
    override def toString = loc + " Warning : " + this.getClass.getSimpleName + " - " + text
  }

  final case class CCREPSYS(loc: Loc, symbol: String) extends Warning {
    // DC doesn't like it
    val text = s"System function '$symbol' called in repetition of constant concatenation"
  }

  final case class BLKSEQ(loc: Loc) extends Warning {
    // DC doesn't like it
    val text = s"Both blocking and non-blocking assignments used in the same always block"
  }

}

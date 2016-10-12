package com.argondesign.alint.warnings

import com.argondesign.alint.Loc
import com.argondesign.alint.SourceAnalysis
import com.argondesign.alint.Source

abstract class Warning {
  val loc: Loc
  val text: String
  override def toString = loc.fileLine + " Warning : " + this.getClass.getSimpleName + " - " + text
}

object Warning {
  implicit object AllWarningsSourceAnalysis extends SourceAnalysis[List[Warning]] {
    def apply(source: Source): List[Warning] = {
      List(
        Warnings[BLKSEQ](source),
        Warnings[CONSTCONCATREP](source),
        Warnings[DNETTYPE](source),
        Warnings[GENNAME](source)).flatten
    }
  }
}

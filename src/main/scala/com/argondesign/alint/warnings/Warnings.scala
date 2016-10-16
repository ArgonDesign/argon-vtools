package com.argondesign.alint.warnings

import com.argondesign.alint.SourceAnalyser
import com.argondesign.alint.Source
import com.argondesign.alint.SourceWarning

object Warnings {
  private val listOfWarnigns: List[SourceAnalyser[List[SourceWarning]]] = List(
    ALWAYSASSIGNMENTS,
    ATSTAR,
    CONSTCONCATREP,
    DNETTYPE,
    GENBEGIN,
    GENNAME,
    GENUNIQ,
    NONAUTOFUNC,
    NORESET,
    RESETSTYLE)

  def apply(source: Source): List[SourceWarning] =
    for (extractor <- listOfWarnigns; warning <- this(extractor)(source))
      yield warning

  def apply[T <: SourceWarning](anayser: SourceAnalyser[List[T]])(source: Source): List[T] = {
    anayser(source)
  }
}

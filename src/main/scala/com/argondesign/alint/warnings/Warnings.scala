package com.argondesign.alint.warnings

import com.argondesign.alint.Source
import com.argondesign.alint.SourceAnalysis
import com.argondesign.alint.SourceWarning

object Warnings {

  def apply(source: Source) = List(
    collect[ATSTAR](source),
    collect[BLKSEQ](source),
    collect[CONSTCONCATREP](source),
    collect[DNETTYPE](source),
    collect[GENBEGIN](source),
    collect[GENNAME](source),
    collect[GENUNIQ](source)).flatten

  def collect[T <: SourceWarning](source: Source)(implicit analysis: SourceAnalysis[List[T]]): List[T] = analysis(source)
}

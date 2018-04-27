////////////////////////////////////////////////////////////////////////////////
// Argon Design Ltd.
// Copyright (c) 2016-2018 Argon Design Ltd. All rights reserved.
//
// This file is covered by the BSD (with attribution) license.
// See the LICENSE file for the precise wording of the license.
//
// Module : Alint
// Author : Geza Lore
//
// DESCRIPTION:
//
////////////////////////////////////////////////////////////////////////////////

package com.argondesign.alint.warnings

import com.argondesign.alint.SourceAnalyser
import com.argondesign.alint.Source
import com.argondesign.alint.SourceWarning

object Warnings {
  private val listOfWarnigns: List[SourceAnalyser[List[SourceWarning]]] = List(
    ALWAYSASSIGNMENTS,
    ANSIMODULE,
    ATSTAR,
    BEGINEND,
    CONSTCONCATREP,
    DNETTYPE,
    GENBEGIN,
    GENNAME,
    GENUNIQ,
    INSTANCEARRAY,
    MANGLEDUNUSED,
    MODULEFILENAME,
    MULTIPLEMODULES,
    NONAUTOFUNC,
    ORDEREDCONNECTION,
    RESETALL,
    RESETSTYLE,
    VARINIT)

  def apply(source: Source): List[SourceWarning] =
    for (extractor <- listOfWarnigns; warning <- this(extractor)(source))
      yield warning

  def apply[T <: SourceWarning](anayser: SourceAnalyser[List[T]])(source: Source): List[T] = {
    anayser(source)
  }
}

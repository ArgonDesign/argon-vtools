////////////////////////////////////////////////////////////////////////////////
// Argon Design Ltd. Project P9000 Argon
// Copyright (c) 2016-2018 Argon Design Ltd. All rights reserved.
//
// This file is covered by the BSD (with attribution) license.
// See the LICENSE file for the precise wording of the license.
//
// Module : Argon Verilog Tools
// Author : Geza Lore
//
// DESCRIPTION:
//
////////////////////////////////////////////////////////////////////////////////

package com.argondesign.vtools

import com.argondesign.vtools.warnings.Warnings

object Lint {
  def apply(sources: List[Source]): Boolean = {
    val lintMessages = {
      for (source <- sources) yield {
        try {
          Warnings(source)
        } catch {
          case SyntaxErrorException(error) => List(error)
        }
      }
    }.flatten

    lintMessages foreach println

    lintMessages.isEmpty
  }

  def apply(conf: CLIConf): Nothing = {
    val pass = Lint(conf.lint.sources())
    sys exit (if (pass) 0 else 1)
  }
}

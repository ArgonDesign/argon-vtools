package com.argondesign.alint

import com.argondesign.alint.warnings.Warnings

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

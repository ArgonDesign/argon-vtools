/*
 * Copyright 2016.
 */

package com.argondesign.alint

import mangle.Mangle
import scalax.file.Path
import com.argondesign.alint.warnings.Warnings

object AlintMain extends App {

  val conf = new CLIConf(args)

  conf.subcommand match {
    case Some(conf.mangle) => Mangle(conf)
    case Some(conf.lint)   => Lint(conf)
    case Some(_)           => ???
    case None => {
      conf.printHelp()
      sys exit 1
    }
  }
}

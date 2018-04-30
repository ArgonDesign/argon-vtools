////////////////////////////////////////////////////////////////////////////////
// Argon Design Ltd.
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

import com.argondesign.vtools.mangle.Mangle

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

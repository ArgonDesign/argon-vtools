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

import org.scalatest.Matchers
import org.scalatest.FlatSpec

class SignalNamesSpec extends FlatSpec with Matchers {
  val rstPrefixes = List("", "a", "b", "n")
  val rstNames = List("rst", "reset")
  val rstSuffixes = List("", "b", "n", "_b", "_n")

  for (p <- rstPrefixes; n <- rstNames; s <- rstSuffixes) {
    val name = p + n + s
    it should s"say $name is reset" in {
      SignalNames.isReset(name) should be(true)
    }
  }

  val nonRst = List("first", "preset")

  for (name <- nonRst) {
    it should s"say $name is not rest" in {
      SignalNames.isReset(name) should be(false)
    }
  }

  val clkPrefixes = List("", "a", "b", "g", "l", "g_", "l_")
  val clkNames = List("clk", "clock")
  val clkSuffixes = List("")

  for (p <- clkPrefixes; n <- clkNames; s <- clkSuffixes) {
    val name = p + n + s
    it should s"say $name is clock" in {
      SignalNames.isClock(name) should be(true)
    }
  }

  val nonClk = List("ack")

  for (name <- nonClk) {
    it should s"say $name is not clock" in {
      SignalNames.isClock(name) should be(false)
    }
  }
}

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

object SignalNames {
  def isClock(name: String) = name matches """(a|b|g|l|g_|l_)?(clk|clock)"""

  def isReset(name: String) = name matches """(a|b|n|a_|b_)?(rst|reset)(b|n|_b|_n)?"""
}

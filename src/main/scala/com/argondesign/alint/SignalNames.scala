package com.argondesign.alint

object SignalNames {
  def isClock(name: String) = name matches """(a|b|g|l|g_|l_)?(clk|clock)"""

  def isReset(name: String) = name matches """(a|b|n|a_|b_)?(rst|reset)(b|n|_b|_n)?"""
}

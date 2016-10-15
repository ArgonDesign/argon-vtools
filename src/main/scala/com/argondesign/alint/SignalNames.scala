package com.argondesign.alint

object SignalNames {
  def isClock(name: String) = name contains "clk"

  def isReset(name: String) = name contains "rst"
}

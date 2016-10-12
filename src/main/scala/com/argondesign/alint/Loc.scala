package com.argondesign.alint

case class Loc(file: String, line: Int, col: Int) {
  def fileLine = file + ":" + line
  override def toString = fileLine + ":" + col
}

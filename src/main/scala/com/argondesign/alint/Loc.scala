package com.argondesign.alint

case class Loc(file: String, line: Int, col: Int) {
  override def toString = file + ":" + line + ":" + col
}

package com.argondesign.alint

trait SourceAnalyser[+T] {
  def apply(source: Source): T
}

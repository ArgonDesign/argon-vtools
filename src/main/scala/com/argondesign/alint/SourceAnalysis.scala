package com.argondesign.alint

trait SourceAnalysis[T] {
  def apply(source: Source): T
}

package com.argondesign.alint.warnings

import com.argondesign.alint.Source
import com.argondesign.alint.SourceAnalysis

object Warnings {
  def apply[T <: Warning](source: Source)(implicit analysis: SourceAnalysis[List[T]]): List[T] = analysis(source)
}

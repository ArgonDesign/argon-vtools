package com.argondesign.alint.warnings

import com.argondesign.alint.Visitor
import com.argondesign.alint.SourceAnalysis
import com.argondesign.alint.Source

class WarningsSourceAnalysisVisitor[T <: Warning]
    extends Visitor[List[T]](Nil, (_ ::: _))
    with SourceAnalysis[List[T]] {
  def apply(source: Source) = visit(source.parseTree)
}

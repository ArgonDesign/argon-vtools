package com.argondesign.alint.warnings

import com.argondesign.alint.Visitor
import com.argondesign.alint.SourceAnalysis
import com.argondesign.alint.Source
import com.argondesign.alint.SourceWarning

class WarningsSourceAnalysisVisitor[T <: SourceWarning]
    extends Visitor[List[T]](Nil, (_ ::: _))
    with SourceAnalysis[List[T]] {
  def apply(source: Source) = visit(source.parseTree)
}

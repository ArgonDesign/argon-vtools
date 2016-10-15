package com.argondesign.alint.warnings

import com.argondesign.alint.Visitor
import com.argondesign.alint.SourceAnalyser
import com.argondesign.alint.Source
import com.argondesign.alint.SourceWarning

class WarningsSourceAnalyserVisitor[T <: SourceWarning]
    extends Visitor[List[T]](Nil, (_ ::: _))
    with SourceAnalyser[List[T]] {
  def apply(source: Source) = visit(source.parseTree)
}

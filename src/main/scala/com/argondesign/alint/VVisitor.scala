package com.argondesign.alint

import org.antlr.v4.runtime.BufferedTokenStream
import org.antlr.v4.runtime.ParserRuleContext

class VVisitor[T](default: T, aggregate: (T, T) => T)
    extends antlr4.VParserBaseVisitor[T]
    with Antlr4Conversions {
  override def defaultResult = default

  override def aggregateResult(prev: T, next: T) = aggregate(prev, next)

  def apply[U <: ParserRuleContext](ctx: U) = visit(ctx)
}

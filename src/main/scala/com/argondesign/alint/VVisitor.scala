package com.argondesign.alint

import org.antlr.v4.runtime.BufferedTokenStream
import org.antlr.v4.runtime.ParserRuleContext

import scala.collection.JavaConversions._

class VVisitor[T](default: T, aggregate: (T, T) => T)
    extends antlr4.VParserBaseVisitor[T]
    with Antlr4Conversions {
  override def defaultResult = default

  override def aggregateResult(prev: T, next: T) = aggregate(prev, next)

  def apply[U <: ParserRuleContext](ctx: U): T = visit(ctx)

  def apply[U <: ParserRuleContext](ctxList: List[U]): T = {
    var result: T = defaultResult();
    for (ctx <- ctxList if shouldVisitNextChild(ctx, result)) {
      result = aggregateResult(result, visit(ctx));
    }
    result
  }

  def apply[U <: ParserRuleContext](ctxList: java.util.List[U]): T = apply(ctxList.toList)
}

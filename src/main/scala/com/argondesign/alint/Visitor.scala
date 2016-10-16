package com.argondesign.alint

import org.antlr.v4.runtime.tree.RuleNode
import org.antlr.v4.runtime.tree.ParseTree

class Visitor[T](override val defaultResult: T, aggregate: (T, T) => T)
    extends antlr4.VParserBaseVisitor[T]
    with antlr4.VParserRuleContexts
    with Antlr4Conversions
    with RestoringConstruct {
  override def aggregateResult(prev: T, next: T) = aggregate(prev, next)

  def apply[U <: RuleNode](ctx: U): T = {
    if (ctx == null) defaultResult else visit(ctx)
  }

  def apply[U <: RuleNode](ctxList: List[U]): T = {
    var result: T = defaultResult;
    for (ctx <- ctxList if shouldVisitNextChild(ctx, result)) {
      result = aggregateResult(result, visit(ctx));
    }
    result
  }

  def apply[U <: RuleNode](ctxList: java.util.List[U]): T = {
    apply(ctxList.toList)
  }
}

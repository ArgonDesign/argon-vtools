package com.argondesign.alint

import org.antlr.v4.runtime.ParserRuleContext

import org.antlr.v4.runtime.tree.RuleNode

class AnyVVisitor extends VVisitor[Boolean](false, (_ || _)) {
  override def shouldVisitNextChild(node: RuleNode, currentResult: Boolean) = !currentResult

}

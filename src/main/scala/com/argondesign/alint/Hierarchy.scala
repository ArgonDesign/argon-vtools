package com.argondesign.alint

import scalax.collection.Graph
import scalax.collection.GraphPredef._
import scalax.collection.GraphEdge._

object Hierarchy {
  object InstanceExtractor extends Visitor[Set[String]](Set(), (_ union _)) {
    override def visitModuleInstantiation(ctx: ModuleInstantiationContext) = Set(ctx.IDENTIFIER)
  }

  object EdgeExtractor extends Visitor[Set[DiEdge[String]]](Set(), (_ union _)) {
    override def visitModuleDeclarationAnsi(ctx: ModuleDeclarationAnsiContext) = {
      val module = ctx.IDENTIFIER.text
      for (instance <- InstanceExtractor(ctx)) yield module ~> instance
    }
  }

  def apply(sources: List[Source]): Graph[String, DiEdge] = {
    val edges = for (source <- sources; edge <- EdgeExtractor(source)) yield edge
    Graph.from(Nil, edges)
  }
}

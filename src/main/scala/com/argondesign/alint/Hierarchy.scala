////////////////////////////////////////////////////////////////////////////////
// Argon Design Ltd.
// Copyright (c) 2016-2018 Argon Design Ltd. All rights reserved.
//
// This file is covered by the BSD (with attribution) license.
// See the LICENSE file for the precise wording of the license.
//
// Module : Argon Verilog Tools
// Author : Geza Lore
//
// DESCRIPTION:
//
////////////////////////////////////////////////////////////////////////////////

package com.argondesign.vtools

import scalax.collection.Graph
import scalax.collection.GraphPredef._
import scalax.collection.GraphEdge._

object Hierarchy {
  object NodeExtractor extends Visitor[Set[String]](Set(), (_ union _)) {
    override def visitModuleDeclarationAnsi(ctx: ModuleDeclarationAnsiContext) = {
      Set(ctx.IDENTIFIER.text)
    }
    override def visitModuleDeclarationNonAnsi(ctx: ModuleDeclarationNonAnsiContext) = {
      Set(ctx.IDENTIFIER.text)
    }
  }

  object InstanceExtractor extends Visitor[Set[String]](Set(), (_ union _)) {
    override def visitModuleInstantiation(ctx: ModuleInstantiationContext) = Set(ctx.IDENTIFIER)
  }

  object EdgeExtractor extends Visitor[Set[DiEdge[String]]](Set(), (_ union _)) {
    override def visitModuleDeclarationAnsi(ctx: ModuleDeclarationAnsiContext) = {
      val module = ctx.IDENTIFIER.text
      for (instance <- InstanceExtractor(ctx)) yield module ~> instance
    }
    override def visitModuleDeclarationNonAnsi(ctx: ModuleDeclarationNonAnsiContext) = {
      val module = ctx.IDENTIFIER.text
      for (instance <- InstanceExtractor(ctx)) yield module ~> instance
    }
  }

  def apply(sources: List[Source]): Graph[String, DiEdge] = {
    val nodes = for (source <- sources; node <- NodeExtractor(source)) yield node
    val edges = for (source <- sources; edge <- EdgeExtractor(source)) yield edge
    Graph.from(nodes, edges)
  }
}

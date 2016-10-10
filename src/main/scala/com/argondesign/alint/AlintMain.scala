/*
 * Copyright 2016.
 */

package com.argondesign.alint

import com.argondesign.alint.antlr4._

import org.antlr.v4.runtime._
import org.antlr.v4.runtime.tree.ParseTreeWalker

import Warnings.CCREPSYS
import Warnings.BLKSEQ

class CCREPSYSVisitor extends WarningsVisitor[CCREPSYS] {
  import com.argondesign.alint.antlr4.VParser._

  object WarnAll extends WarningsVisitor[CCREPSYS] {
    override def visitConstantSystemFunctionCall(ctx: ConstantSystemFunctionCallContext) = {
      CCREPSYS(ctx.start.loc, ctx.SYSID.text) :: visitChildren(ctx)
    }
  }

  override def visitConstantMultipleConcatenation(ctx: ConstantMultipleConcatenationContext) = {
    WarnAll(ctx.constantExpression) ::: visitChildren(ctx.constantConcatenation)
  }
}

class BLKSEQVisitor extends WarningsVisitor[BLKSEQ] {
  import com.argondesign.alint.antlr4.VParser._

  object AnyBlockingAssignments extends AnyVVisitor {
    override def visitBlockingAssignment(ctx: BlockingAssignmentContext) = true
  }

  object AnyNonBlockingAssignments extends AnyVVisitor {
    override def visitNonblockingAssignment(ctx: NonblockingAssignmentContext) = true
  }

  override def visitAlwaysConstruct(ctx: AlwaysConstructContext) = {
    if (AnyBlockingAssignments(ctx) && AnyNonBlockingAssignments(ctx)) {
      BLKSEQ(ctx.start.loc) :: visitChildren(ctx)
    } else {
      visitChildren(ctx)
    }
  }
}

object AlintMain extends App {
  import Antlr4Conversions._

  for (inputFile <- args) {
    val inputStream = new ANTLRFileStream(inputFile)
    val lexer = new VLexer(inputStream)
    val tokenStream = new CommonTokenStream(lexer)
    val parser = new VParser(tokenStream)
    val tree = parser.start()

    val visitor = new CCREPSYSVisitor
    for (warning <- visitor.visit(tree)) {
      println(warning)
    }

    {
      val visitor = new BLKSEQVisitor
      for (warning <- visitor.visit(tree)) {
        println(warning)
      }
    }
  }
}

/*
 * Copyright 2016.
 */

package com.argondesign.alint

import Warnings.CCREPSYS

class CCREPSYSVisitor extends WarningsVisitor[CCREPSYS] {
  import com.argondesign.alint.antlr4.VParser._

  object WarnIn extends WarningsVisitor[CCREPSYS] {
    override def visitConstantSystemFunctionCall(ctx: ConstantSystemFunctionCallContext) = {
      CCREPSYS(ctx.loc, ctx.SYSID.text) :: visitChildren(ctx)
    }
  }

  override def visitConstantMultipleConcatenation(ctx: ConstantMultipleConcatenationContext) = {
    WarnIn(ctx.constantExpression) ::: visitChildren(ctx.constantConcatenation)
  }
}

import Warnings.BLKSEQ

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
      BLKSEQ(ctx.loc) :: visitChildren(ctx)
    } else {
      visitChildren(ctx)
    }
  }
}

import Warnings.GENNAME

class GENNAMEVisitor extends WarningsVisitor[GENNAME] {
  import com.argondesign.alint.antlr4.VParser._

  override def visitGenerateBlock(ctx: GenerateBlockContext) = {
    if (ctx.IDENTIFIER eq null) {
      GENNAME(ctx.loc) :: visitChildren(ctx)
    } else {
      visitChildren(ctx)
    }
  }
}

object AlintMain extends App {
  import org.antlr.v4.runtime._

  for (inputFile <- args) {
    val inputStream = new ANTLRFileStream(inputFile)
    val lexer = new antlr4.VLexer(inputStream)
    val tokenStream = new CommonTokenStream(lexer)
    val parser = new antlr4.VParser(tokenStream)
    val tree = parser.start()

    val visitors = List(
      new CCREPSYSVisitor,
      new BLKSEQVisitor,
      new GENNAMEVisitor)

    for (visitor <- visitors) {
      for (warning <- visitor.visit(tree)) {
        println(warning)
      }
    }
  }
}

package com.argondesign.alint.warnings

import com.argondesign.alint.Loc
import com.argondesign.alint.Source
import com.argondesign.alint.SourceAnalyser
import com.argondesign.alint.SourceWarning
import com.argondesign.alint.Visitor

final case class BLKSEQ(val loc: Loc, subtype: Int) extends SourceWarning {
  val message = subtype match {
    case 0 => "Blocking assignment used in sequential always block"
    case 1 => "Non blocking assignment used in combinatorial always block"
  }
}

object BLKSEQ extends SourceAnalyser[List[BLKSEQ]] {
  object BLKSEQSourceAnalysisVisitor extends WarningsSourceAnalysisVisitor[BLKSEQ] {
    object AnyBlockingAssignments extends Visitor[Boolean](false, _ || _) {
      override def visitBlockingAssignment(ctx: BlockingAssignmentContext) = true
    }

    object AnyNonBlockingAssignments extends Visitor[Boolean](false, _ || _) {
      override def visitNonblockingAssignment(ctx: NonblockingAssignmentContext) = true
    }

    override def visitAlwaysEvent(ctx: AlwaysEventContext) = {
      if (AnyBlockingAssignments(ctx)) {
        BLKSEQ(ctx.loc, 0) :: visitChildren(ctx)
      } else {
        visitChildren(ctx)
      }
    }

    override def visitAlwaysAtStar(ctx: AlwaysAtStarContext) = {
      if (AnyNonBlockingAssignments(ctx)) {
        BLKSEQ(ctx.loc, 1) :: visitChildren(ctx)
      } else {
        visitChildren(ctx)
      }
    }
  }

  def apply(source: Source) = BLKSEQSourceAnalysisVisitor(source)
}

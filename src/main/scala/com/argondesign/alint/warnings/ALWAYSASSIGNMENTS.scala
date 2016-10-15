package com.argondesign.alint.warnings

import com.argondesign.alint.Loc
import com.argondesign.alint.Source
import com.argondesign.alint.SourceAnalyser
import com.argondesign.alint.SourceWarning
import com.argondesign.alint.Visitor

final case class ALWAYSASSIGNMENTS(val loc: Loc, subtype: Int) extends SourceWarning {
  val message = subtype match {
    case 0 => "Blocking assignment used in sequential always block"
    case 1 => "Non blocking assignment used in combinatorial always block"
  }
}

object ALWAYSASSIGNMENTS extends SourceAnalyser[List[ALWAYSASSIGNMENTS]] {
  object BLKSEQSourceAnalysisVisitor extends WarningsSourceAnalyserVisitor[ALWAYSASSIGNMENTS] {
    object AnyBlockingAssignments extends Visitor[Boolean](false, _ || _) {
      override def visitBlockingAssignment(ctx: BlockingAssignmentContext) = true
    }

    object AnyNonBlockingAssignments extends Visitor[Boolean](false, _ || _) {
      override def visitNonblockingAssignment(ctx: NonblockingAssignmentContext) = true
    }

    override def visitAlwaysEvent(ctx: AlwaysEventContext) = {
      if (AnyBlockingAssignments(ctx)) List(ALWAYSASSIGNMENTS(ctx.loc, 0)) else Nil
    }

    override def visitAlwaysAtStar(ctx: AlwaysAtStarContext) = {
      if (AnyNonBlockingAssignments(ctx)) List(ALWAYSASSIGNMENTS(ctx.loc, 1)) else Nil
    }
  }

  def apply(source: Source) = BLKSEQSourceAnalysisVisitor(source)
}

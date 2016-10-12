package com.argondesign.alint.warnings

import com.argondesign.alint.Loc
import com.argondesign.alint.Visitor

final case class BLKSEQ(val loc: Loc) extends Warning {
  val text = s"Both blocking and non-blocking assignments used in the same always block"
}

object BLKSEQ {
  implicit object BLKSEQSourceAnalysisVisitor extends WarningsSourceAnalysisVisitor[BLKSEQ] {
    import com.argondesign.alint.antlr4.VParser._

    object AnyBlockingAssignments extends Visitor[Boolean](false, _ || _) {
      override def visitBlockingAssignment(ctx: BlockingAssignmentContext) = true
    }

    object AnyNonBlockingAssignments extends Visitor[Boolean](false, _ || _) {
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
}

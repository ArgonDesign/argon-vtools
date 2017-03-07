package com.argondesign.alint.warnings

import com.argondesign.alint.Loc
import com.argondesign.alint.Source
import com.argondesign.alint.SourceAnalyser
import com.argondesign.alint.SourceWarning
import org.antlr.v4.runtime.Token

final case class ORDEREDCONNECTION(val loc: Loc) extends SourceWarning {
  val message = "Do not use ordered port/parameter connections"
}

object ORDEREDCONNECTION extends SourceAnalyser[List[ORDEREDCONNECTION]] {
  object ORDEREDCONNECTIONSourceAnalyserVisitor extends WarningsSourceAnalyserVisitor[ORDEREDCONNECTION] {
    override def visitOrderedParameterAssignment(ctx: OrderedParameterAssignmentContext) = List(ORDEREDCONNECTION(ctx.loc))
    override def visitOrderedPortConnection(ctx: OrderedPortConnectionContext) = List(ORDEREDCONNECTION(ctx.loc))
  }

  def apply(source: Source) = ORDEREDCONNECTIONSourceAnalyserVisitor(source)
}

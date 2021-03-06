////////////////////////////////////////////////////////////////////////////////
// Argon Design Ltd. Project P9000 Argon
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

package com.argondesign.vtools.warnings

import com.argondesign.vtools.Loc
import com.argondesign.vtools.Source
import com.argondesign.vtools.SourceAnalyser
import com.argondesign.vtools.SourceWarning
import org.antlr.v4.runtime.ParserRuleContext
import com.argondesign.vtools.antlr4.VLexer

import scala.collection.JavaConverters._
import org.antlr.v4.runtime.tree.TerminalNode

final case class MANGLEDUNUSED(val loc: Loc, name: String) extends SourceWarning {
  val message = s"Variable name containing substring 'unused' should have /* verilator lint_on UNUSED */ aplied: '$name'"
}

object MANGLEDUNUSED extends SourceAnalyser[List[MANGLEDUNUSED]] {
  def apply(source: Source) = {

    object MANGLEDUNUSEDSourceAnalyserVisitor extends WarningsSourceAnalyserVisitor[MANGLEDUNUSED] {
      val commentTokenTypes = Set[java.lang.Integer](VLexer.ONE_LINE_COMMENT, VLexer.BLOCK_COMMENT).asJava

      def check(token: TerminalNode) = {
        val name = token.text
        if (name.toLowerCase contains "unused") {
          val precedingComments = source.tokenStream.getTokens(0, token.index, commentTokenTypes) match {
            case null => Nil
            case some => some.asScala.toList
          }
          val signoff = precedingComments.reverse map (_.text) collectFirst {
            case s if s matches "/\\*.*verilator.*lint_on.*UNUSED.*\\*/"  => false
            case s if s matches "//.*verilator.*lint_on.*UNUSED\\s*"      => false
            case s if s matches "/\\*.*verilator.*lint_off.*UNUSED.*\\*/" => true
            case s if s matches "//.*verilator.*lint_off.*UNUSED\\s*"     => true
          }
          signoff match {
            case Some(true) => Nil
            case _          => List(MANGLEDUNUSED(token.loc, name))
          }
        } else {
          Nil
        }
      }

      override def visitVariableTypeUnpacked(ctx: VariableTypeUnpackedContext) = check(ctx.IDENTIFIER)
      override def visitVariableTypeInitialised(ctx: VariableTypeInitialisedContext) = check(ctx.IDENTIFIER)
      override def visitNetDeclAssignment(ctx: NetDeclAssignmentContext) = check(ctx.IDENTIFIER)
      override def visitListOfNetIdentifiers(ctx: ListOfNetIdentifiersContext) = ctx.IDENTIFIER.toList flatMap check
    }

    MANGLEDUNUSEDSourceAnalyserVisitor(source)
  }
}

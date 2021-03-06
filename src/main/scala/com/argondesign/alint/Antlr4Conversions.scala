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

package com.argondesign.vtools

import scala.collection.convert.WrapAsJava
import scala.collection.convert.WrapAsScala
import scala.language.implicitConversions

import org.antlr.v4.runtime.ParserRuleContext
import org.antlr.v4.runtime.Token
import org.antlr.v4.runtime.misc.Interval
import org.antlr.v4.runtime.tree.ParseTree
import org.antlr.v4.runtime.tree.TerminalNode
import org.antlr.v4.runtime.tree.Tree

trait Antlr4Conversions extends WrapAsScala with WrapAsJava {
  implicit class ParserRuleContextWrapper(val ctx: ParserRuleContext) {
    def sourceText: String = {
      val inputStream = ctx.start.getInputStream
      val startIdx = ctx.start.getStartIndex
      val stopIdx = ctx.stop.getStopIndex

      val leadingLen = ctx.start.getCharPositionInLine
      val trailingLen = inputStream.getText(Interval.of(stopIdx + 1, stopIdx + 200)).takeWhile(_ != '\n').length

      val filler = "\u2591"

      val leading = filler * leadingLen
      val source = inputStream.getText(Interval.of(startIdx, stopIdx))
      val trailing = filler * trailingLen

      leading + source + trailing
    }

    def text = ctx.getText

    def loc = ctx.start.loc
  }

  implicit class TokenWrapper(val token: Token) {
    def loc = Loc(token.getTokenSource.getSourceName, token.getLine, token.getCharPositionInLine)

    def text = token.getText

    def index = token.getTokenIndex

    def isHidden = token.getChannel != Token.DEFAULT_CHANNEL
  }

  implicit class ParseTreeWrapper(val node: ParseTree) {
    def text = node.getText

    def children: List[ParseTree] = {
      for (n <- 0 to node.getChildCount - 1)
        yield node.getChild(n)
    }.toList
  }

  implicit class TreeWrapper(val node: Tree) {
    private[this] def descendantOf(self: Tree, that: Tree): Boolean = {
      var parent = self.getParent
      while (parent != null) {
        if (parent == that) return true
        parent = parent.getParent
      }
      false
    }

    def -<-(that: Tree): Boolean = descendantOf(node, that)
    def !<-(that: Tree): Boolean = !descendantOf(node, that)
    def ->-(that: Tree): Boolean = descendantOf(that, node)
    def !>-(that: Tree): Boolean = !descendantOf(that, node)
  }

  implicit def terminalNodeToString(node: TerminalNode): String = node.text
  implicit def terminalNodeToToken(node: TerminalNode): Token = node.getSymbol
  implicit def terminalNodeToTokenWrapper(node: TerminalNode): TokenWrapper = new TokenWrapper(node.getSymbol)
}

object Antlr4Conversions extends Antlr4Conversions {}

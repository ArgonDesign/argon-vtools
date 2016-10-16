package com.argondesign.alint

import scala.collection.convert.WrapAsJava
import scala.collection.convert.WrapAsScala
import scala.language.implicitConversions

import org.antlr.v4.runtime.ParserRuleContext
import org.antlr.v4.runtime.Token
import org.antlr.v4.runtime.misc.Interval
import org.antlr.v4.runtime.tree.ParseTree
import org.antlr.v4.runtime.tree.TerminalNode

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
  }

  implicit class ParseTreeWrapper(val node: ParseTree) {
    def text = node.getText

    def children: List[ParseTree] = {
      for (n <- 0 to node.getChildCount - 1)
        yield node.getChild(n)
    }.toList

  }

  implicit def terminalNoteToString(node: TerminalNode): String = node.text
}

object Antlr4Conversions extends Antlr4Conversions {}

package com.argondesign.alint

import scala.collection.JavaConversions._

import org.antlr.v4.runtime.Token
import org.antlr.v4.runtime.ParserRuleContext
import org.antlr.v4.runtime.BufferedTokenStream
import org.antlr.v4.runtime.misc.Interval
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.ANTLRFileStream
import com.argondesign.alint.antlr4.VLexer
import org.antlr.v4.runtime.tree.TerminalNode

trait Antlr4Conversions {
  implicit class RichParserRuleContext(val ctx: ParserRuleContext) {
    lazy val tokenStream = {
      val ts = new CommonTokenStream(new VLexer(new ANTLRFileStream(loc.file)))
      ts.fill()
      ts
    }

    def sourceText: String = {
      val filler = "\u2591"
      val leading = filler * ctx.start.loc.col
      val source = tokenStream.getText(ctx)
      val stopIndex = ctx.stop.getTokenIndex
      val trailingLen = tokenStream.getText(Interval.of(stopIndex + 1, stopIndex + 20)).takeWhile(_ != '\n').length
      val trailing = filler * trailingLen
      leading + source + trailing
    }

    def loc = ctx.start.loc
  }

  implicit class RichToken(val token: Token) {
    def loc = Loc(token.getTokenSource.getSourceName, token.getLine, token.getCharPositionInLine)

    def text = token.getText
  }

  implicit class RichTerminalNode(val node: TerminalNode) {
    lazy val text = node.getSymbol.text
  }

}

object Antlr4Conversions extends Antlr4Conversions {}

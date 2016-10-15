package com.argondesign.alint

import scala.collection.JavaConversions._
import org.antlr.v4.runtime.{ ANTLRInputStream, CommonTokenStream }
import org.antlr.v4.runtime.Token

class Source(val name: String, val text: String) {

  def this(name: String) = {
    this(name, scala.io.Source.fromFile(name).mkString)
  }

  lazy val tokenStream = {
    val inputStream = new ANTLRInputStream(text)
    inputStream.name = name
    val lexer = new antlr4.VLexer(inputStream)
    val tokenStream = new CommonTokenStream(lexer)
    tokenStream.fill()
    tokenStream
  }

  lazy val tokens = tokenStream.getTokens.toList

  lazy val parseTree = {
    val parser = new antlr4.VParser(tokenStream)
    parser.removeErrorListeners()
    parser.addErrorListener(new VParserErrorListener)
    parser.start()
  }
}

object Source {
  def apply(name: String, text: String) = new Source(name, text)
  def apply(name: String) = new Source(name)
}

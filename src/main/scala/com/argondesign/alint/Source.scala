package com.argondesign.alint

import org.antlr.v4.runtime.{ ANTLRInputStream, CommonTokenStream }

class Source(val name: String, val text: String) {

  def this(name: String) = {
    this(name, scala.io.Source.fromFile(name).mkString)
  }

  lazy val parseTree = {
    val inputStream = new ANTLRInputStream(text)
    inputStream.name = name
    val lexer = new antlr4.VLexer(inputStream)
    val tokenStream = new CommonTokenStream(lexer)
    val parser = new antlr4.VParser(tokenStream)
    parser.start()
  }
}

object Source {
  def apply(name: String, text: String) = new Source(name, text)
  def apply(name: String) = new Source(name)
}

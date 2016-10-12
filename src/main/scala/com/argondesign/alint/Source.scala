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

  //  lazy val warnings: List[Warning] = {
  //    List(
  //      warnings(CCREPSYS),
  //      warnings(BLKSEQ),
  //      warnings(GENNAME),
  //      warnings(DNTMISSING),
  //      warnings(DNTLOC)).flatten
  //  }
  //
  //  private def warnings[T](kind: T): List[Warning] = kind match {
  //    case CCREPSYS   => (new CCREPSYSVisitor).visit(parseTree)
  //    case BLKSEQ     => (new BLKSEQVisitor).visit(parseTree)
  //    case GENNAME    => (new GENNAMEVisitor).visit(parseTree)
  //    case DNTMISSING => (new DNTMISSINGVisitor).visit(parseTree)
  //    case DNTLOC     => (new DNTLOCVisitor).visit(parseTree)
  //  }
}

object Source {
  def apply(name: String, text: String) = new Source(name, text)
  def apply(name: String) = new Source(name)
}

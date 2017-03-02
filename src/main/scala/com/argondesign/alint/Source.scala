package com.argondesign.alint

import org.antlr.v4.runtime.{ ANTLRInputStream, CommonTokenStream }
import org.antlr.v4.runtime.Token
import org.antlr.v4.runtime.ParserRuleContext
import scala.collection.mutable.Stack

import com.argondesign.alint.Antlr4Conversions._
import scalax.file.Path

class Source(val path: Path, val text: String) {

  def this(path: Path) = {
    this(path, path.lines(includeTerminator = true).mkString)
  }

  def this(path: String, text: String) = {
    this(Path.fromString(path), text)
  }

  def this(path: String) = {
    this(Path.fromString(path))
  }

  val name = path.name

  lazy val tokenStream = {
    val inputStream = new ANTLRInputStream(text)
    inputStream.name = name
    val lexer = new antlr4.VLexer(inputStream)
    val tokenStream = new CommonTokenStream(lexer)
    tokenStream.fill()
    tokenStream
  }

  lazy val tokens: List[Token] = tokenStream.getTokens.toList

  def hiddenTokensToRight(token: Token): List[Token] = {
    val hiddenTokens = tokenStream.getHiddenTokensToRight(token.index)
    if (hiddenTokens == null) Nil else hiddenTokens.toList
  }

  lazy val parseTree = {
    val parser = new antlr4.VParser(tokenStream)
    parser.removeErrorListeners()
    parser.addErrorListener(new VParserErrorListener)
    parser.start()
  }

  lazy val ctxNames = {
    object CollectNames extends Visitor[Map[ParserRuleContext, String]](Map(), _ ++ _) {
      val nameStack = Stack[String]()
      val countStack = Stack[Int]()
      var count = 0

      override def visitGenerateBlockWithBeginEnd(ctx: GenerateBlockWithBeginEndContext) = {
        if (ctx.IDENTIFIER != null) {
          nameStack.push(ctx.IDENTIFIER)
        } else {
          nameStack.push("genblk" + count); count += 1;
        }
        countStack push count; count = 0

        visitChildren(ctx) + (ctx -> nameStack.reverse.mkString("."))
      } restoring {
        count = countStack.pop()
        nameStack.pop()
      }
    }

    CollectNames.visit(parseTree)
  }

}

object Source {
  def apply(path: Path, text: String) = new Source(path, text)
  def apply(path: Path) = new Source(path)
  def apply(path: String, text: String) = new Source(path, text)
  def apply(path: String) = new Source(path)
}

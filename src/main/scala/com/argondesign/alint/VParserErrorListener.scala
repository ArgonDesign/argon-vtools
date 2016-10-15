package com.argondesign.alint

import org.antlr.v4.runtime.BaseErrorListener
import org.antlr.v4.runtime.RecognitionException
import org.antlr.v4.runtime.Recognizer

class VParserErrorListener extends BaseErrorListener {
  override def syntaxError(recognizer: Recognizer[_, _],
                           offendingSymbol: Object,
                           line: Int,
                           charPositionInLine: Int,
                           msg: String,
                           e: RecognitionException) = {
    val loc = Loc(recognizer.getInputStream.getSourceName, line, charPositionInLine)
    throw new SyntaxErrorException(loc, "Syntax error - " + msg)
  }
}

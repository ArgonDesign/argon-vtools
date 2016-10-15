package com.argondesign.alint

final case class SyntaxError(val loc: Loc, val message: String) extends SourceError {}

case class SyntaxErrorException(val error: SyntaxError) extends Exception {

  def this(loc: Loc, message: String) = this(SyntaxError(loc, message))
}

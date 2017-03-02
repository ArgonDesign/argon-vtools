package com.argondesign.alint

sealed abstract trait Message {
  val category: String
  val message: String

  override def toString = category + ": " + this.getClass.getSimpleName + " - " + message
}

abstract trait Warning extends Message {
  val category = "WARNING"
}

abstract trait Error extends Message {
  val category = "ERROR"
}

sealed abstract trait SourceMessage extends Message {
  val loc: Loc

  override def toString = loc + " " + super.toString
}

abstract trait SourceWarning extends SourceMessage with Warning;

abstract trait SourceError extends SourceMessage with Error;
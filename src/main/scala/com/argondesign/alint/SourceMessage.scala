package com.argondesign.alint

import warnings.Warnings

sealed abstract class SourceMessage {
  val loc: Loc
  val category: String
  val message: String

  override def toString = loc + " " + category + ": " + this.getClass.getSimpleName + " - " + message
}

abstract class SourceWarning extends SourceMessage {
  val category = "WARNING"
}

abstract class SourceError extends SourceMessage {
  val category = "ERROR"
}

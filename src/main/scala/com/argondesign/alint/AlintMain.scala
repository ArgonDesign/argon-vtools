/*
 * Copyright 2016.
 */

package com.argondesign.alint

import warnings._

object AlintMain extends App {
  import org.antlr.v4.runtime._

  val messages = for (fileName <- args) yield {
    try {
      Warnings(Source(fileName))
    } catch {
      case SyntaxErrorException(error) => List(error)
    }
  }

  messages.flatten foreach println

  //  System exit (if (messages.flatten.isEmpty) 0 else 1)
}

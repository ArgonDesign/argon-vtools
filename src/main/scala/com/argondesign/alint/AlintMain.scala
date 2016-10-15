/*
 * Copyright 2016.
 */

package com.argondesign.alint

import warnings.Warnings

object AlintMain extends App {

  val messages = {
    for (fileName <- args.toList; source = Source(fileName)) yield {
      try {
        Warnings(source)
      } catch {
        case SyntaxErrorException(error) => List(error)
      }
    }
  }.flatten

  messages foreach println

  sys exit (if (messages.isEmpty) 0 else 1)
}

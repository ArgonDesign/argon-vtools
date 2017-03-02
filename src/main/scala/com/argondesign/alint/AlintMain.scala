/*
 * Copyright 2016.
 */

package com.argondesign.alint

import scalax.file.Path

import warnings.Warnings
import mangle.Mangle

object AlintMain extends App {

  // TODO: Proper argument parsing
  val filePaths = if (args.head == "--mangle") args.tail.toList else args.toList

  val sources = filePaths map { Source(_) }

  val lintMessages = {
    for (source <- sources) yield {
      try {
        Warnings(source)
      } catch {
        case SyntaxErrorException(error) => List(error)
      }
    }
  }.flatten

  if (!lintMessages.isEmpty) {
    lintMessages foreach println
    sys exit 1
  }

  if (args(0) == "--mangle") {
    for (mangledSource <- Mangle(sources)) {
      Path("out", mangledSource.name).write(mangledSource.text)
    }
  }

  sys exit 0
}

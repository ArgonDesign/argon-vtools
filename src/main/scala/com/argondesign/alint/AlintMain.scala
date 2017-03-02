/*
 * Copyright 2016.
 */

package com.argondesign.alint

import mangle.Mangle
import scalax.file.Path
import com.argondesign.alint.warnings.Warnings

object AlintMain extends App {

  // TODO: Proper argument parsing
  val filePaths = {
    if (args.head == "--mangle") args.tail.toList else args.toList
  } map { Path.fromString(_) }

  case class FILEMISSING(name: String) extends Error {
    val message = s"Input file '$name' does not exist"
  }

  val messages = for (filePath <- filePaths if filePath.nonExistent) yield {
    FILEMISSING(filePath.path)
  }

  if (!messages.isEmpty) {
    messages foreach println
    sys exit 1
  }

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

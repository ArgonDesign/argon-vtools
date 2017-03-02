/*
 * Copyright 2016.
 */

package com.argondesign.alint

import scalax.file.Path

import warnings.Warnings
import mangle.Mangle

object AlintMain extends App {

  def lint(fileNames: List[String]) = {
    val messages = {
      for (fileName <- fileNames; source = Source(fileName)) yield {
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

  def mangle(fileNames: List[String]) = {
    try {
      val sources = fileNames map { Source(_) }
      val mangledSources = Mangle(sources)
      for (mangledSource <- mangledSources) {
        Path("out", mangledSource.name).write(mangledSource.text)
      }
    } catch {
      case SyntaxErrorException(error) => {
        println(error)
        sys exit 1
      }
    }
  }

  if (args(0) == "--mangle") {
    mangle(args.toList.tail)
  } else {
    lint(args.toList)
  }
}

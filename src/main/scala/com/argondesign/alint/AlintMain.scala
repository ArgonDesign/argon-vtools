/*
 * Copyright 2016.
 */

package com.argondesign.alint

import warnings._

object AlintMain extends App {
  import org.antlr.v4.runtime._

  val warnings = for (fileName <- args)
    yield Warnings(Source(fileName))

  warnings.flatten foreach println

  //  System exit (if (warnings.flatten.isEmpty) 0 else 1)
}

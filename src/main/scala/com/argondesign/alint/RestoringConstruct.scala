package com.argondesign.alint

import scala.language.implicitConversions

final class RestoringWrapper[A](private val self: A) extends AnyVal {
  def restoring(block: => Any): A = { block; self }
}

trait RestoringConstruct {
  implicit def anyToRestoring[A](self: A) = new RestoringWrapper(self)
}

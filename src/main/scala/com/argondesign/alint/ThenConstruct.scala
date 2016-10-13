package com.argondesign.alint

final class ThenWrapper[A](private val self: A) extends AnyVal {
  def then(block: => Any): A = { block; self }
}

trait ThenConstruct {
  implicit def anyToThen[A](self: A) = new ThenWrapper(self)
}

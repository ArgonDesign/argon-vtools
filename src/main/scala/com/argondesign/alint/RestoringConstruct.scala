////////////////////////////////////////////////////////////////////////////////
// Argon Design Ltd. Project P9000 Argon
// Copyright (c) 2016-2018 Argon Design Ltd. All rights reserved.
//
// This file is covered by the BSD (with attribution) license.
// See the LICENSE file for the precise wording of the license.
//
// Module : Argon Verilog Tools
// Author : Geza Lore
//
// DESCRIPTION:
//
////////////////////////////////////////////////////////////////////////////////

package com.argondesign.vtools

import scala.language.implicitConversions

final class RestoringWrapper[A](private val self: A) extends AnyVal {
  def restoring(block: => Any): A = { block; self }
}

trait RestoringConstruct {
  implicit def anyToRestoring[A](self: A) = new RestoringWrapper(self)
}

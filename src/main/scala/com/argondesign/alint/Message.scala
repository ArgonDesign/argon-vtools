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

sealed abstract trait Message {
  val category: String
  val message: String

  override def toString = category + ": " + this.getClass.getSimpleName.takeWhile(_ != '$') + " - " + message
}

abstract trait Warning extends Message {
  val category = "WARNING"
}

abstract trait Error extends Message {
  val category = "ERROR"
}

sealed abstract trait SourceMessage extends Message {
  val loc: Loc

  override def toString = loc + " " + super.toString
}

abstract trait SourceWarning extends SourceMessage with Warning;

abstract trait SourceError extends SourceMessage with Error;
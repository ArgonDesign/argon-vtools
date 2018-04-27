////////////////////////////////////////////////////////////////////////////////
// Argon Design Ltd.
// Copyright (c) 2016-2018 Argon Design Ltd. All rights reserved.
//
// This file is covered by the BSD (with attribution) license.
// See the LICENSE file for the precise wording of the license.
//
// Module : Alint
// Author : Geza Lore
//
// DESCRIPTION:
//
////////////////////////////////////////////////////////////////////////////////

package com.argondesign.alint

final case class SyntaxError(val loc: Loc, val message: String) extends SourceError {}

case class SyntaxErrorException(val error: SyntaxError) extends Exception {
  def this(loc: Loc, message: String) = this(SyntaxError(loc, message))
}

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

package com.argondesign.alint.warnings

import org.scalatest._
import com.argondesign.alint.Source
import com.argondesign.alint.Loc

class VARINITSpec extends FlatSpec with Matchers {
  "VARINIT" should "be detected for 'reg' declaration with initalisation in module scope" in {
    val text = """|module foo;
                  |  reg a = 0;
                  |endmodule
                  |""".stripMargin
    val warnings = Warnings(VARINIT)(Source("test.v", text))

    warnings should have length 1

    warnings.head should be(VARINIT(Loc("test.v", 2, 6), "a"))
  }

  it should "not be detected for 'reg' declaration without initialization in module scope" in {
    val text = """|module foo;
                  |  reg a;
                  |endmodule
                  |""".stripMargin
    val warnings = Warnings(VARINIT)(Source("test.v", text))

    warnings should have length 0
  }
}

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

package com.argondesign.vtools.warnings

import org.scalatest._
import com.argondesign.vtools.Source
import com.argondesign.vtools.Loc

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

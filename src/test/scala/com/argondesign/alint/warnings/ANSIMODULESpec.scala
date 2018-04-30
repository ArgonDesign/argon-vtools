////////////////////////////////////////////////////////////////////////////////
// Argon Design Ltd.
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

class ANSIMODULESpec extends FlatSpec with Matchers {
  "ANSIMODULE" should "be detected for non-ANSI style module declarations" in {
    val text = """|module foo (a, b);
                  |endmodule
                  |""".stripMargin
    val warnings = Warnings(ANSIMODULE)(Source("test.v", text))

    warnings should have length 1

    warnings.head should be(ANSIMODULE(Loc("test.v", 1, 7), "foo"))
  }

  it should "not be detected for ANSI style module declarations" in {
    val text = """|module foo(input a, output b);
                  |endmodule
                  |""".stripMargin
    val warnings = Warnings(ANSIMODULE)(Source("test.v", text))

    warnings should have length 0
  }
}

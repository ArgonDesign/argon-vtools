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

class MANGLEDUNUSEDSpec extends FlatSpec with Matchers {
  "MANGLEDUNUSED" should "be detected for variable definition containing 'unused' without a lint_off directive" in {
    val text = """|module foo;
                  |  reg aunusedb;
                  |endmodule
                  |""".stripMargin
    val warnings = Warnings(MANGLEDUNUSED)(Source("test.v", text))

    warnings should have length 1

    warnings.head should be(MANGLEDUNUSED(Loc("test.v", 2, 6), "aunusedb"))
  }

  it should "be detected for variable definition containing 'Unused' without a lint_off directive" in {
    val text = """|module foo;
                  |  reg aUnusedb;
                  |endmodule
                  |""".stripMargin
    val warnings = Warnings(MANGLEDUNUSED)(Source("test.v", text))

    warnings should have length 1

    warnings.head should be(MANGLEDUNUSED(Loc("test.v", 2, 6), "aUnusedb"))
  }

  it should "not be detected for variable definition not containing 'unused'" in {
    val text = """|module foo;
                  |  reg used;
                  |endmodule
                  |""".stripMargin
    val warnings = Warnings(MANGLEDUNUSED)(Source("test.v", text))

    warnings should have length 0
  }

  it should "not be detected for variable definition containing 'unused' with a lint_off directive" in {
    val text = """|module foo;
                  |  /* verilator lint_off UNUSED */
                  |  reg unused;
                  |endmodule
                  |""".stripMargin
    val warnings = Warnings(MANGLEDUNUSED)(Source("test.v", text))

    warnings should have length 0
  }

  it should "be detected for net definition containing 'unused' without a lint_off directive" in {
    val text = """|module foo;
                  |  wire aunusedb;
                  |endmodule
                  |""".stripMargin
    val warnings = Warnings(MANGLEDUNUSED)(Source("test.v", text))

    warnings should have length 1

    warnings.head should be(MANGLEDUNUSED(Loc("test.v", 2, 7), "aunusedb"))
  }

  it should "be detected for net definition with initializer containing 'unused' without a lint_off directive" in {
    val text = """|module foo;
                  |  wire aunusedb = 1'b1;
                  |endmodule
                  |""".stripMargin
    val warnings = Warnings(MANGLEDUNUSED)(Source("test.v", text))

    warnings should have length 1

    warnings.head should be(MANGLEDUNUSED(Loc("test.v", 2, 7), "aunusedb"))
  }

  it should "be detected for net definition containing 'Unused' without a lint_off directive" in {
    val text = """|module foo;
                  |  wire aUnusedb;
                  |endmodule
                  |""".stripMargin
    val warnings = Warnings(MANGLEDUNUSED)(Source("test.v", text))

    warnings should have length 1

    warnings.head should be(MANGLEDUNUSED(Loc("test.v", 2, 7), "aUnusedb"))
  }

  it should "not be detected for net definition not containing 'unused'" in {
    val text = """|module foo;
                  |  wire used;
                  |endmodule
                  |""".stripMargin
    val warnings = Warnings(MANGLEDUNUSED)(Source("test.v", text))

    warnings should have length 0
  }

  it should "not be detected for net definition containing 'unused' with a lint_off directive" in {
    val text = """|module foo;
                  |  /* verilator lint_off UNUSED */
                  |  wire unused;
                  |endmodule
                  |""".stripMargin
    val warnings = Warnings(MANGLEDUNUSED)(Source("test.v", text))

    warnings should have length 0
  }

  it should "be detected for net definition containing 'unused' with a lint_on directive" in {
    val text = """|module foo;
                  |  /* verilator lint_off UNUSED */
                  |  /* verilator lint_on UNUSED */
                  |  wire unused;
                  |endmodule
                  |""".stripMargin
    val warnings = Warnings(MANGLEDUNUSED)(Source("test.v", text))

    warnings should have length 1

    warnings.head should be(MANGLEDUNUSED(Loc("test.v", 4, 7), "unused"))
  }

}

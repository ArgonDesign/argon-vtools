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
import com.argondesign.alint.Loc
import com.argondesign.alint.Source

class MULTIPLEMODULESSpec extends FlatSpec with Matchers {
  "MULTIPLEMODULES" should "be detected for multiple non-ANSI module declarations in the same file" in {
    val text = """|module foo (a, b);
                  |endmodule
                  |module bar (a, b);
                  |endmodule
                  |""".stripMargin
    val warnings = Warnings(MULTIPLEMODULES)(Source("test.v", text))

    warnings should have length 2

    warnings(0) should be(MULTIPLEMODULES(Loc("test.v", 1, 7), "foo"))
    warnings(1) should be(MULTIPLEMODULES(Loc("test.v", 3, 7), "bar"))
  }

  it should "be detected for multiple ANSI module declarations in the same file" in {
    val text = """|module foo (input a, output b);
                  |endmodule
                  |module bar (input a, output b);
                  |endmodule
                  |""".stripMargin
    val warnings = Warnings(MULTIPLEMODULES)(Source("test.v", text))

    warnings should have length 2

    warnings(0) should be(MULTIPLEMODULES(Loc("test.v", 1, 7), "foo"))
    warnings(1) should be(MULTIPLEMODULES(Loc("test.v", 3, 7), "bar"))
  }

  it should "be detected for mixed style module declarations in the same file" in {
    val text = """|module foo (input a, output b);
                  |endmodule
                  |module bar (a, b);
                  |endmodule
                  |""".stripMargin
    val warnings = Warnings(MULTIPLEMODULES)(Source("test.v", text))

    warnings should have length 2

    warnings(0) should be(MULTIPLEMODULES(Loc("test.v", 1, 7), "foo"))
    warnings(1) should be(MULTIPLEMODULES(Loc("test.v", 3, 7), "bar"))
  }

  it should "not be detected for single non-ANSI module declaration in file" in {
    val text = """|module foo(a, b);
                  |endmodule
                  |""".stripMargin
    val warnings = Warnings(MULTIPLEMODULES)(Source("foo.v", text))

    warnings should have length 0
  }

  it should "not be detected for single ANSI module declaration in file" in {
    val text = """|module foo(input a, output b);
                  |endmodule
                  |""".stripMargin
    val warnings = Warnings(MULTIPLEMODULES)(Source("foo.v", text))

    warnings should have length 0
  }
}

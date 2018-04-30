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
import com.argondesign.vtools.Loc
import com.argondesign.vtools.Source

class MODULEFILENAMESpec extends FlatSpec with Matchers {
  "MODULEFILENAME" should "be detected for non-ANSI module declarations not matching file name" in {
    val text = """|module foo (a, b);
                  |endmodule
                  |""".stripMargin
    val warnings = Warnings(MODULEFILENAME)(Source("test.v", text))

    warnings should have length 1

    warnings.head should be(MODULEFILENAME(Loc("test.v", 1, 7), "foo"))
  }

  it should "be detected for ANSI module declarations not matching file name" in {
    val text = """|module foo (input a, output b);
                  |endmodule
                  |""".stripMargin
    val warnings = Warnings(MODULEFILENAME)(Source("test.v", text))

    warnings should have length 1

    warnings.head should be(MODULEFILENAME(Loc("test.v", 1, 7), "foo"))
  }

  it should "not be detected for non-ANSI module declarations matching file name" in {
    val text = """|module foo(a, b);
                  |endmodule
                  |""".stripMargin
    val warnings = Warnings(MODULEFILENAME)(Source("foo.v", text))

    warnings should have length 0
  }

  it should "not be detected for ANSI module declarations matching file name" in {
    val text = """|module foo(input a, output b);
                  |endmodule
                  |""".stripMargin
    val warnings = Warnings(MODULEFILENAME)(Source("foo.v", text))

    warnings should have length 0
  }

  it should "not be detected for ANSI module declarations matching file name wiht path" in {
    val text = """|module foo(input a, output b);
                  |endmodule
                  |""".stripMargin
    val warnings = Warnings(MODULEFILENAME)(Source("bar/foo.v", text))

    warnings should have length 0
  }
}

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

class NONAUTOFUNCSpec extends FlatSpec with Matchers {
  "NONAUTOFUNC" should "be detected for non-automatic functions" in {
    val text = """|module foo;
                  |  function func(input a);
                  |    func = a;
                  |  endfunction
                  |endmodule
                  |""".stripMargin
    val warnings = Warnings(NONAUTOFUNC)(Source("test.v", text))

    warnings should have length 1

    warnings.head should be(NONAUTOFUNC(Loc("test.v", 2, 2), "func"))
  }

  it should "not be detected for automatic functions" in {
    val text = """|module foo;
                  |  function automatic func(input a);
                  |    func = a;
                  |  endfunction
                  |endmodule
                  |""".stripMargin
    val warnings = Warnings(NONAUTOFUNC)(Source("test.v", text))

    warnings should have length 0
  }
}

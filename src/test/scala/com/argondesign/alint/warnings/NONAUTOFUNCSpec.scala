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

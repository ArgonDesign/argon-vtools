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

class INSTANCEARRAYSpec extends FlatSpec with Matchers {
  "INSTANCEARRAY" should "be detected for array instances" in {
    val text = """|module foo ();
                  |  bar bar_i [3:0] ();
                  |endmodule
                  |""".stripMargin
    val warnings = Warnings(INSTANCEARRAY)(Source("test.v", text))

    warnings should have length 1

    warnings.head should be(INSTANCEARRAY(Loc("test.v", 2, 6)))
  }

  it should "not be detected for non-array instances" in {
    val text = """|module foo();
                  |  bar bar_i ();
                  |endmodule
                  |""".stripMargin
    val warnings = Warnings(INSTANCEARRAY)(Source("test.v", text))

    warnings should have length 0
  }
}

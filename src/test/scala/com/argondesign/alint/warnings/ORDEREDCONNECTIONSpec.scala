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

class ORDEREDCONNECTIONSpec extends FlatSpec with Matchers {
  "ORDEREDCONNECTION" should "be detected for ordered port connection" in {
    val text = """|module foo ();
                  |  bar bar_i (clk);
                  |endmodule
                  |""".stripMargin
    val warnings = Warnings(ORDEREDCONNECTION)(Source("test.v", text))

    warnings should have length 1

    warnings.head should be(ORDEREDCONNECTION(Loc("test.v", 2, 13)))
  }

  it should "be detected for ordered parameter connection" in {
    val text = """|module foo ();
                  |  bar #(W) bar_i ();
                  |endmodule
                  |""".stripMargin
    val warnings = Warnings(ORDEREDCONNECTION)(Source("test.v", text))

    warnings should have length 1

    warnings.head should be(ORDEREDCONNECTION(Loc("test.v", 2, 8)))
  }

  it should "not be detected for named port connection" in {
    val text = """|module foo();
                  |  bar bar_i (.clk(clk));
                  |endmodule
                  |""".stripMargin
    val warnings = Warnings(ORDEREDCONNECTION)(Source("test.v", text))

    warnings should have length 0
  }

  it should "not be detected for named paramter connection" in {
    val text = """|module foo();
                  |  bar #(.W(W)) bar_i ();
                  |endmodule
                  |""".stripMargin
    val warnings = Warnings(ORDEREDCONNECTION)(Source("test.v", text))

    warnings should have length 0
  }

}

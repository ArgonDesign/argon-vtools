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

package com.argondesign.vtools

import org.scalatest.Matchers
import org.scalatest.FlatSpec

class SytnaxErrorSpec extends FlatSpec with Matchers {

  val incorrectInput = """|module foo;
                          |  if (1) begin
                          |    assign a = 0;
                          |endmodule
                          |""".stripMargin

  "SyntaxErrorException" should "be thrown for incorrect input" in {
    a[SyntaxErrorException] should be thrownBy {
      Source("test.v", incorrectInput).parseTree
    }
  }

  it should "have loc set to the location of the error" in {
    val exception = the[SyntaxErrorException] thrownBy {
      Source("test.v", incorrectInput).parseTree
    }

    exception.error.loc should be(Loc("test.v", 4, 0))
  }
}

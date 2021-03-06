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

import org.scalatest.FlatSpec
import org.scalatest.Matchers

import com.argondesign.vtools.Source
import com.argondesign.vtools.Loc

class GENUNIQSpec extends FlatSpec with Matchers {
  "GENUNIQ" should "not be detected when generate block names are unique" in {
    val text = """|module foo;
                  |  generate for (a = 0 ; a ; a = a) begin : name
                  |  end endgenerate
                  |  generate for (a = 0 ; a ; a = a) begin : name2
                  |  end endgenerate
                  |  generate for (a = 0 ; a ; a = a) begin : name3
                  |  end endgenerate
                  |endmodule
                  |""".stripMargin
    val warnings = Warnings(GENUNIQ)(Source("test.v", text))

    warnings should have length 0
  }

  it should "not be detected for unnamed generate blocks" in {
    val text = """|module foo;
                  |  generate for (a = 0 ; a ; a = a) begin
                  |  end endgenerate
                  |  generate for (a = 0 ; a ; a = a) begin
                  |  end endgenerate
                  |  generate for (a = 0 ; a ; a = a) begin
                  |  end endgenerate
                  |endmodule
                  |""".stripMargin
    val warnings = Warnings(GENUNIQ)(Source("test.v", text))

    warnings should have length 0
  }

  it should "be detected for generate blocks with the same name in the same scope" in {
    val text = """|module foo;
                  |  generate for (a = 0 ; a ; a = a) begin : name
                  |  end endgenerate
                  |  generate for (a = 0 ; a ; a = a) begin : name
                  |  end endgenerate
                  |  generate for (a = 0 ; a ; a = a) begin : name
                  |  end endgenerate
                  |endmodule
                  |""".stripMargin
    val warnings = Warnings(GENUNIQ)(Source("test.v", text))

    warnings should have length 3
    warnings(0) should be(GENUNIQ(Loc("test.v", 2, 35), "name"))
    warnings(1) should be(GENUNIQ(Loc("test.v", 4, 35), "name"))
    warnings(2) should be(GENUNIQ(Loc("test.v", 6, 35), "name"))
  }

  it should "not be detected for generate blocks with the same name in different scopes" in {
    val text = """|module foo;
                  |  generate for (a = 0 ; a ; a = a) begin : name
                  |    for (a = 0 ; a ; a = a) begin : name
                  |      for (a = 0 ; a ; a = a) begin : name
                  |      end
                  |    end
                  |  end endgenerate
                  |endmodule
                  |""".stripMargin
    val warnings = Warnings(GENUNIQ)(Source("test.v", text))

    warnings should have length 0
  }

  it should "not be detected for generate blocks with the same name in different scopes - unnamed" in {
    val text = """|module foo;
                  |  generate for (a = 0 ; a ; a = a) begin
                  |    for (a = 0 ; a ; a = a) begin : name
                  |      for (a = 0 ; a ; a = a) begin : name
                  |      end
                  |    end
                  |  end endgenerate
                  |  generate for (a = 0 ; a ; a = a) begin
                  |    for (a = 0 ; a ; a = a) begin : name
                  |      for (a = 0 ; a ; a = a) begin : name
                  |      end
                  |    end
                  |  end endgenerate
                  |endmodule
                  |""".stripMargin
    val warnings = Warnings(GENUNIQ)(Source("test.v", text))

    warnings should have length 0
  }
}

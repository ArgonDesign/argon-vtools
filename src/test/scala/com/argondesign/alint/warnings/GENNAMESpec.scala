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

class GENNAMESpec extends FlatSpec with Matchers {

  "GENNAME" should "not be detected in named generate for inside generate endgenerate" in {
    val text = """|module foo;
                  |  generate for (a = 0 ; a ; a = a) begin : name
                  |  end endgenerate
                  |endmodule
                  |""".stripMargin
    val warnings = Warnings(GENNAME)(Source("test.v", text))

    warnings should have length 0
  }

  it should "not be detected in named generate for without generate endgenerate" in {
    val text = """|module foo;
                  |  for (a = 0 ; a ; a = a) begin : name
                  |  end
                  |endmodule
                  |""".stripMargin
    val warnings = Warnings(GENNAME)(Source("test.v", text))

    warnings should have length 0
  }

  it should "be detected in unnamed generate for inside generate endgenerate" in {
    val text = """|module foo;
                  |  generate for (a = 0 ; a ; a = a) begin
                  |  end endgenerate
                  |endmodule
                  |""".stripMargin
    val warnings = Warnings(GENNAME)(Source("test.v", text))

    warnings should have length 1

    warnings.head should be(GENNAME(Loc("test.v", 2, 35)))
  }

  it should "be detected in unnamed generate for without generate endgenerate" in {
    val text = """|module foo;
                  |  for (a = 0 ; a ; a = a) begin
                  |  end
                  |endmodule
                  |""".stripMargin
    val warnings = Warnings(GENNAME)(Source("test.v", text))

    warnings should have length 1

    warnings.head should be(GENNAME(Loc("test.v", 2, 26)))
  }

  it should "not be detected in named generate if inside generate endgenerate" in {
    val text = """|module foo;
                  |  generate if (a) begin : name
                  |  end endgenerate
                  |endmodule
                  |""".stripMargin
    val warnings = Warnings(GENNAME)(Source("test.v", text))

    warnings should have length 0
  }

  it should "not be detected in named generate if without generate endgenerate" in {
    val text = """|module foo;
                  |  if (a) begin : name
                  |  end
                  |endmodule
                  |""".stripMargin
    val warnings = Warnings(GENNAME)(Source("test.v", text))

    warnings should have length 0
  }

  it should "be detected in unnamed generate if inside generate endgenerate" in {
    val text = """|module foo;
                  |  generate if (a) begin
                  |  end endgenerate
                  |endmodule
                  |""".stripMargin
    val warnings = Warnings(GENNAME)(Source("test.v", text))

    warnings should have length 1

    warnings.head should be(GENNAME(Loc("test.v", 2, 18)))
  }

  it should "be detected in unnamed generate if without generate endgenerate" in {
    val text = """|module foo;
                  |  if (a) begin
                  |  end
                  |endmodule
                  |""".stripMargin
    val warnings = Warnings(GENNAME)(Source("test.v", text))

    warnings should have length 1

    warnings.head should be(GENNAME(Loc("test.v", 2, 9)))
  }

  it should "not be detected in named generate else inside generate endgenerate" in {
    val text = """|module foo;
                  |  generate if (a) begin : name
                  |    end else begin : name
                  |  end endgenerate
                  |endmodule
                  |""".stripMargin
    val warnings = Warnings(GENNAME)(Source("test.v", text))

    warnings should have length 0
  }

  it should "not be detected in named generate else without generate endgenerate" in {
    val text = """|module foo;
                  |  if (a) begin : name
                  |  end else begin : name
                  |  end
                  |endmodule
                  |""".stripMargin
    val warnings = Warnings(GENNAME)(Source("test.v", text))

    warnings should have length 0
  }

  it should "be detected in unnamed generate else inside generate endgenerate" in {
    val text = """|module foo;
                  |  generate if (a) begin : name
                  |    end else begin
                  |  end endgenerate
                  |endmodule
                  |""".stripMargin
    val warnings = Warnings(GENNAME)(Source("test.v", text))

    warnings should have length 1

    warnings.head should be(GENNAME(Loc("test.v", 3, 13)))
  }

  it should "be detected in unnamed generate else without generate endgenerate" in {
    val text = """|module foo;
                  |  if (a) begin : name
                  |  end else begin
                  |  end
                  |endmodule
                  |""".stripMargin
    val warnings = Warnings(GENNAME)(Source("test.v", text))

    warnings should have length 1

    warnings.head should be(GENNAME(Loc("test.v", 3, 11)))
  }

  it should "not be detected in named generate else if inside generate endgenerate" in {
    val text = """|module foo;
                  |  generate if (a) begin : name
                  |    end else if (b) begin : name
                  |  end endgenerate
                  |endmodule
                  |""".stripMargin
    val warnings = Warnings(GENNAME)(Source("test.v", text))

    warnings should have length 0
  }

  it should "not be detected in named generate else if without generate endgenerate" in {
    val text = """|module foo;
                  |  if (a) begin : name
                  |  end else if (b) begin : name
                  |  end
                  |endmodule
                  |""".stripMargin
    val warnings = Warnings(GENNAME)(Source("test.v", text))

    warnings should have length 0
  }

  it should "be detected in unnamed generate else if inside generate endgenerate" in {
    val text = """|module foo;
                  |  generate if (a) begin : name
                  |    end else if (b) begin
                  |  end endgenerate
                  |endmodule
                  |""".stripMargin
    val warnings = Warnings(GENNAME)(Source("test.v", text))

    warnings should have length 1

    warnings.head should be(GENNAME(Loc("test.v", 3, 20)))
  }

  it should "be detected in unnamed generate else if without generate endgenerate" in {
    val text = """|module foo;
                  |  if (a) begin : name
                  |  end else if (b) begin
                  |  end
                  |endmodule
                  |""".stripMargin
    val warnings = Warnings(GENNAME)(Source("test.v", text))

    warnings should have length 1

    warnings.head should be(GENNAME(Loc("test.v", 3, 18)))
  }

  it should "not be detected in named generate case item inside generate endgenerate" in {
    val text = """|module foo;
                  |  generate
                  |    case(a)
                  |      a: begin: name
                  |      end
                  |    endcase
                  |  endgenerate
                  |endmodule
                  |""".stripMargin
    val warnings = Warnings(GENNAME)(Source("test.v", text))

    warnings should have length 0
  }

  it should "not be detected in named generate case item without generate endgenerate" in {
    val text = """|module foo;
                  |  case(a)
                  |    a: begin: name
                  |    end
                  |  endcase
                  |endmodule
                  |""".stripMargin
    val warnings = Warnings(GENNAME)(Source("test.v", text))

    warnings should have length 0
  }

  it should "be detected in unnamed generate case item inside generate endgenerate" in {
    val text = """|module foo;
                  |  generate
                  |     case(a)
                  |      a: begin
                  |      end
                  |    endcase
                  |  endgenerate
                  |endmodule
                  |""".stripMargin
    val warnings = Warnings(GENNAME)(Source("test.v", text))

    warnings should have length 1

    warnings.head should be(GENNAME(Loc("test.v", 4, 9)))
  }

  it should "be detected in unnamed generate case item without generate endgenerate" in {
    val text = """|module foo;
                  |  case(a)
                  |    a: begin
                  |    end
                  |  endcase
                  |endmodule
                  |""".stripMargin
    val warnings = Warnings(GENNAME)(Source("test.v", text))

    warnings should have length 1

    warnings.head should be(GENNAME(Loc("test.v", 3, 7)))
  }
}

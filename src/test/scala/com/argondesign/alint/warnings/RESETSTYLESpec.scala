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

import org.scalatest.Matchers
import org.scalatest.FlatSpec
import com.argondesign.vtools.Source
import com.argondesign.vtools.Loc

class RESETSTYLESpec extends FlatSpec with Matchers {

  "RESETSTYLE" should "not be detected for well formed 'if (rst)' statements" in {
    val text = """|module foo;
                  |  always @(posedge clk) begin
                  |    if (rst) begin
                  |    end else begin
                  |    end
                  |  end
                  |endmodule
                  |""".stripMargin
    val warnings = Warnings(RESETSTYLE)(Source("test.v", text))

    warnings should have length 0
  }

  it should "not be detected for well formed 'if (!rst)' statements" in {
    val text = """|module foo;
                  |  always @(posedge clk) begin
                  |    if (!rst) begin
                  |    end else begin
                  |    end
                  |  end
                  |endmodule
                  |""".stripMargin
    val warnings = Warnings(RESETSTYLE)(Source("test.v", text))

    warnings should have length 0
  }

  it should "be detected for late 'if (rst)' statements" in {
    val text = """|module foo;
                  |  always @(posedge clk) begin
                  |    a <= 0;
                  |    if (rst) begin
                  |    end
                  |  end
                  |endmodule
                  |""".stripMargin
    val warnings = Warnings(RESETSTYLE)(Source("test.v", text))

    warnings should have length 1

    warnings.head should be(RESETSTYLE(Loc("test.v", 4, 8)))
  }

  it should "be detected for trailing statements" in {
    val text = """|module foo;
                  |  always @(posedge clk) begin
                  |    if (rst) begin
                  |    end
                  |    a <= 0;
                  |  end
                  |endmodule
                  |""".stripMargin
    val warnings = Warnings(RESETSTYLE)(Source("test.v", text))

    warnings should have length 1

    warnings.head should be(RESETSTYLE(Loc("test.v", 5, 4)))
  }

  it should "be detected for 'else if (rst)' statements" in {
    val text = """|module foo;
                  |  always @(posedge clk) begin
                  |    if (a) begin
                  |    end else if (rst) begin
                  |    end
                  |  end
                  |endmodule
                  |""".stripMargin
    val warnings = Warnings(RESETSTYLE)(Source("test.v", text))

    warnings should have length 1

    warnings.head should be(RESETSTYLE(Loc("test.v", 4, 17)))
  }

  it should "be detected for 'if (rst)' statement nested inside 'if'" in {
    val text = """|module foo;
                  |  always @(posedge clk) begin
                  |    if (a) begin
                  |      if (rst) begin
                  |      end else begin
                  |      end
                  |    end
                  |  end
                  |endmodule
                  |""".stripMargin
    val warnings = Warnings(RESETSTYLE)(Source("test.v", text))

    warnings should have length 1

    warnings.head should be(RESETSTYLE(Loc("test.v", 4, 10)))
  }

  it should "be detected for 'if (rst)' statement nested inside 'else if'" in {
    val text = """|module foo;
                  |  always @(posedge clk) begin
                  |    if (a) begin
                  |    end else if (b) begin
                  |      if (rst) begin
                  |      end else begin
                  |      end
                  |    end
                  |  end
                  |endmodule
                  |""".stripMargin
    val warnings = Warnings(RESETSTYLE)(Source("test.v", text))

    warnings should have length 1

    warnings.head should be(RESETSTYLE(Loc("test.v", 5, 10)))
  }

  it should "be detected for 'if (rst)' statement nested inside 'else'" in {
    val text = """|module foo;
                  |  always @(posedge clk) begin
                  |    if (a) begin
                  |    end else if (b) begin
                  |    end else begin
                  |      if (rst) begin
                  |      end else begin
                  |      end
                  |    end
                  |  end
                  |endmodule
                  |""".stripMargin
    val warnings = Warnings(RESETSTYLE)(Source("test.v", text))

    warnings should have length 1

    warnings.head should be(RESETSTYLE(Loc("test.v", 6, 10)))
  }

  it should "be detected for 'if (rst)' statement nested inside 'case'" in {
    val text = """|module foo;
                  |  always @(posedge clk) begin
                  |    case (a)
                  |    1:  if (rst) begin
                  |        end
                  |    endcase
                  |  end
                  |endmodule
                  |""".stripMargin
    val warnings = Warnings(RESETSTYLE)(Source("test.v", text))

    warnings should have length 1

    warnings.head should be(RESETSTYLE(Loc("test.v", 4, 12)))
  }

  it should "be detected for 'if (rst)' statement nested inside 'for' loop" in {
    val text = """|module foo;
                  |  always @(posedge clk) begin
                  |    for (a=1;a;a=1) if (rst) begin
                  |    end
                  |  end
                  |endmodule
                  |""".stripMargin
    val warnings = Warnings(RESETSTYLE)(Source("test.v", text))

    warnings should have length 1

    warnings.head should be(RESETSTYLE(Loc("test.v", 3, 24)))
  }

  it should "be detected for 'if (rst)' statement nested inside 'while' loop" in {
    val text = """|module foo;
                  |  always @(posedge clk) begin
                  |    while(a) if (rst) begin
                  |    end
                  |  end
                  |endmodule
                  |""".stripMargin
    val warnings = Warnings(RESETSTYLE)(Source("test.v", text))

    warnings should have length 1

    warnings.head should be(RESETSTYLE(Loc("test.v", 3, 17)))
  }

  it should "be detected for 'if (rst)' statement nested inside 'repeat' loop" in {
    val text = """|module foo;
                  |  always @(posedge clk) begin
                  |    repeat(a) if (rst) begin
                  |    end
                  |  end
                  |endmodule
                  |""".stripMargin
    val warnings = Warnings(RESETSTYLE)(Source("test.v", text))

    warnings should have length 1

    warnings.head should be(RESETSTYLE(Loc("test.v", 3, 18)))
  }

  it should "be detected for 'if (rst)' statement nested inside 'forever' loop" in {
    val text = """|module foo;
                  |  always @(posedge clk) begin
                  |    forever if (rst) begin
                  |    end
                  |  end
                  |endmodule
                  |""".stripMargin
    val warnings = Warnings(RESETSTYLE)(Source("test.v", text))

    warnings should have length 1

    warnings.head should be(RESETSTYLE(Loc("test.v", 3, 16)))
  }

  it should "be detected in example 1" in {
    val text = """|module foo;
                  |  always @(posedge clk or negedge rst_n) begin
                  |    a <= 288'b0;
                  |    if (!rst_n) begin
                  |      a <= 288'b0;
                  |    end else begin
                  |      a <= cr_rot_mat;
                  |    end
                  |  end
                  |endmodule
                  |""".stripMargin
    val warnings = Warnings(RESETSTYLE)(Source("test.v", text))

    warnings should have length 1

    warnings.head should be(RESETSTYLE(Loc("test.v", 4, 8)))
  }
}

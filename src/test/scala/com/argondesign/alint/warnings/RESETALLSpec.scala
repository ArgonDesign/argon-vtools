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
import com.argondesign.vtools.SyntaxErrorException

class RESETALLSpec extends FlatSpec with Matchers {

  "RESETALL" should "not be detected for signals assigned in the reset clause" in {
    val text = """|module foo;
                  |  always @(posedge clk) begin
                  |    if (rst) begin
                  |      a <= 0;
                  |    end else begin
                  |      a <= 1;
                  |    end
                  |  end
                  |endmodule
                  |""".stripMargin
    val warnings = Warnings(RESETALL)(Source("test.v", text))

    warnings should have length 0
  }

  it should "not be detected for signals assigned in the reset clause, and partially assigned in the body" in {
    val text = """|module foo;
                  |  always @(posedge clk) begin
                  |    if (!rst) begin
                  |      a <= 0;
                  |    end else begin
                  |      a[0] <= 0;
                  |    end
                  |  end
                  |endmodule
                  |""".stripMargin
    val warnings = Warnings(RESETALL)(Source("test.v", text))

    warnings should have length 0
  }

  it should "not be detected in blocks without a reset clause" in {
    val text = """|module foo;
                  |  always @(posedge clk) begin
                  |    a <= 0;
                  |  end
                  |endmodule
                  |""".stripMargin
    val warnings = Warnings(RESETALL)(Source("test.v", text))

    warnings should have length 0
  }

  it should "not be detected for indexed assignment" in {
    val text = """|module foo;
                  |  always @(posedge clk) begin
                  |    if (!rst) begin
                  |      a <= 0;
                  |    end else begin
                  |      a[index] <= 0;
                  |    end
                  |  end
                  |endmodule
                  |""".stripMargin
    val warnings = Warnings(RESETALL)(Source("test.v", text))

    warnings should have length 0
  }

  it should "not be detected for indexed and sliced assignment" in {
    val text = """|module foo;
                  |  always @(posedge clk) begin
                  |    if (!rst) begin
                  |      a <= 0;
                  |    end else begin
                  |      a[p][q][r][s:t] <= 0;
                  |    end
                  |  end
                  |endmodule
                  |""".stripMargin
    val warnings = Warnings(RESETALL)(Source("test.v", text))

    warnings should have length 0
  }

  it should "be detected for signals not assigned in the reset clause" in {
    val text = """|module foo;
                  |  always @(posedge clk) begin
                  |    if (!rst_n) begin
                  |    end else begin
                  |      b <= 0;
                  |    end
                  |  end
                  |endmodule
                  |""".stripMargin
    val warnings = Warnings(RESETALL)(Source("test.v", text))

    warnings should have length 1

    warnings.head should be(RESETALL(Loc("test.v", 5, 6), "b", 0))
  }

  it should "be detected for signals not assigned in the reset clause - partial assignment" in {
    val text = """|module foo;
                  |  always @(posedge clk) begin
                  |    if (!rst_n) begin
                  |    end else begin
                  |      b[0] <= 0;
                  |    end
                  |  end
                  |endmodule
                  |""".stripMargin
    val warnings = Warnings(RESETALL)(Source("test.v", text))

    warnings should have length 1

    warnings.head should be(RESETALL(Loc("test.v", 5, 6), "b", 0))
  }

  it should "be detected for signals not assigned in the reset clause - unpacking assignment" in {
    val text = """|module foo;
                  |  always @(posedge clk) begin
                  |    if (!rst_n) begin
                  |      a <= 0;
                  |    end else begin
                  |      {a, b} <= 1;
                  |    end
                  |  end
                  |endmodule
                  |""".stripMargin
    val warnings = Warnings(RESETALL)(Source("test.v", text))

    warnings should have length 1

    warnings.head should be(RESETALL(Loc("test.v", 6, 10), "b", 0))
  }

  it should "be detected for signals not assigned in the reset clause - nested assignment" in {
    val text = """|module foo;
                  |  always @(posedge clk) begin
                  |    if (!rst_n) begin
                  |    end else begin
                  |      if (c) b <= 1;
                  |    end
                  |  end
                  |endmodule
                  |""".stripMargin
    val warnings = Warnings(RESETALL)(Source("test.v", text))

    warnings should have length 1

    warnings.head should be(RESETALL(Loc("test.v", 5, 13), "b", 0))
  }

  it should "be detected for signals not assigned in non-standard reset clause" in {
    val text = """|module foo;
                  |  always @(posedge clk) begin
                  |    a <= 0;
                  |    b <= 1;
                  |    if (!rst_n) begin
                  |      a <= 0;
                  |    end
                  |  end
                  |endmodule
                  |""".stripMargin
    val warnings = Warnings(RESETALL)(Source("test.v", text))

    warnings should have length 1

    warnings.head should be(RESETALL(Loc("test.v", 4, 4), "b", 0))
  }

  it should "be detected for signals not assigned in non-standard 'else if' reset clause" in {
    val text = """|module foo;
                  |  always @(posedge clk) begin
                  |    a <= 0;
                  |    b <= 1;
                  |    if (a) begin
                  |    end else if (!rst_n) begin
                  |      a <= 0;
                  |    end
                  |  end
                  |endmodule
                  |""".stripMargin
    val warnings = Warnings(RESETALL)(Source("test.v", text))

    warnings should have length 1

    warnings.head should be(RESETALL(Loc("test.v", 4, 4), "b", 0))
  }

  it should "be detected for signals only assigned in the reset clause" in {
    val text = """|module foo;
                  |  always @(posedge clk) begin
                  |    if (!rst_n) begin
                  |      a <= 0;
                  |    end else begin
                  |    end
                  |  end
                  |endmodule
                  |""".stripMargin
    val warnings = Warnings(RESETALL)(Source("test.v", text))

    warnings should have length 1

    warnings.head should be(RESETALL(Loc("test.v", 4, 6), "a", 1))
  }
}

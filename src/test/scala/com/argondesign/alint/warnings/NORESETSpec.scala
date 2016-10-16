package com.argondesign.alint.warnings

import org.scalatest.Matchers
import org.scalatest.FlatSpec
import com.argondesign.alint.Source
import com.argondesign.alint.Loc
import com.argondesign.alint.SyntaxErrorException

class NORESETSpec extends FlatSpec with Matchers {

  "NORESET" should "not be detected for signals assigned in the reset clause" in {
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
    val warnings = Warnings(NORESET)(Source("test.v", text))

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
    val warnings = Warnings(NORESET)(Source("test.v", text))

    warnings should have length 0
  }

  it should "not be detected in blocks without a reset clause" in {
    val text = """|module foo;
                  |  always @(posedge clk) begin
                  |    a <= 0;
                  |  end
                  |endmodule
                  |""".stripMargin
    val warnings = Warnings(NORESET)(Source("test.v", text))

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
    val warnings = Warnings(NORESET)(Source("test.v", text))

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
    val warnings = Warnings(NORESET)(Source("test.v", text))

    warnings should have length 0
  }

  it should "be detected for signals not assigned in the reset clause" in {
    val text = """|module foo;
                  |  always @(posedge clk) begin
                  |    if (!rst_n) begin
                  |    end else begin
                  |      a <= 0;
                  |    end
                  |  end
                  |endmodule
                  |""".stripMargin
    val warnings = Warnings(NORESET)(Source("test.v", text))

    warnings should have length 1

    warnings.head should be(NORESET(Loc("test.v", 5, 6), "a"))
  }

  it should "be detected for signals not assigned in the reset clause - partial assignment" in {
    val text = """|module foo;
                  |  always @(posedge clk) begin
                  |    if (!rst_n) begin
                  |      a <= 0;
                  |    end else begin
                  |      b[0] <= 0;
                  |    end
                  |  end
                  |endmodule
                  |""".stripMargin
    val warnings = Warnings(NORESET)(Source("test.v", text))

    warnings should have length 1

    warnings.head should be(NORESET(Loc("test.v", 6, 6), "b"))
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
    val warnings = Warnings(NORESET)(Source("test.v", text))

    warnings should have length 1

    warnings.head should be(NORESET(Loc("test.v", 6, 10), "b"))
  }

  it should "be detected for signals not assigned in the reset clause - nested assignment" in {
    val text = """|module foo;
                  |  always @(posedge clk) begin
                  |    if (!rst_n) begin
                  |      a <= 0;
                  |    end else begin
                  |      if (c) b <= 1;
                  |    end
                  |  end
                  |endmodule
                  |""".stripMargin
    val warnings = Warnings(NORESET)(Source("test.v", text))

    warnings should have length 1

    warnings.head should be(NORESET(Loc("test.v", 6, 13), "b"))
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
    val warnings = Warnings(NORESET)(Source("test.v", text))

    warnings should have length 1

    warnings.head should be(NORESET(Loc("test.v", 4, 4), "b"))
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
    val warnings = Warnings(NORESET)(Source("test.v", text))

    warnings should have length 1

    warnings.head should be(NORESET(Loc("test.v", 4, 4), "b"))
  }
}

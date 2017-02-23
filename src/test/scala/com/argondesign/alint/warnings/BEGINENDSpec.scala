package com.argondesign.alint.warnings

import org.scalatest._
import com.argondesign.alint.Source
import com.argondesign.alint.Loc

class BEGINENDSpec extends FlatSpec with Matchers {
  "BEGINEND" should "be detected for 'always @*' without begin/end" in {
    val text = """|module foo;
                  |  always @*
                  |    if (x) begin
                  |      a = b;
                  |    end
                  |endmodule
                  |""".stripMargin
    val warnings = Warnings(BEGINEND)(Source("test.v", text))

    warnings should have length 1

    warnings.head should be(BEGINEND(Loc("test.v", 3, 4), "always @*"))
  }

  it should "not be detected for 'always @*' with begin/end" in {
    val text = """|module foo;
                  |  always @* begin a = b; end
                  |endmodule
                  |""".stripMargin
    val warnings = Warnings(BEGINEND)(Source("test.v", text))

    warnings should have length 0
  }

  it should "be detected for 'always @(...)' without begin/end" in {
    val text = """|module foo;
                  |  always @(posedge clk) a = b;
                  |endmodule
                  |""".stripMargin
    val warnings = Warnings(BEGINEND)(Source("test.v", text))

    warnings should have length 1

    warnings.head should be(BEGINEND(Loc("test.v", 2, 24), "always @(...)"))
  }

  it should "not be detected for 'always @(...)' without begin/end" in {
    val text = """|module foo;
                  |  always @(posedge clk) begin a = b; end
                  |endmodule
                  |""".stripMargin
    val warnings = Warnings(BEGINEND)(Source("test.v", text))

    warnings should have length 0
  }

  it should "be detected for 'if (...)' without begin/end" in {
    val text = """|module foo;
                  |  always @* begin
                  |    if (x) a = b;
                  |  end
                  |endmodule
                  |""".stripMargin
    val warnings = Warnings(BEGINEND)(Source("test.v", text))

    warnings should have length 1

    warnings.head should be(BEGINEND(Loc("test.v", 3, 11), "if (...)"))
  }

  it should "not be detected for 'if (...)' with begin/end" in {
    val text = """|module foo;
                  |  always @* begin
                  |    if (x) begin a = b; end
                  |  end
                  |endmodule
                  |""".stripMargin
    val warnings = Warnings(BEGINEND)(Source("test.v", text))

    warnings should have length 0
  }

  it should "be detected for 'else if (...)' without begin/end" in {
    val text = """|module foo;
                  |  always @* begin
                  |    if (x) begin a = b; end
                  |    else if (y) b = a;
                  |  end
                  |endmodule
                  |""".stripMargin
    val warnings = Warnings(BEGINEND)(Source("test.v", text))

    warnings should have length 1

    warnings.head should be(BEGINEND(Loc("test.v", 4, 16), "else if (...)"))
  }

  it should "not be detected for 'else if (...)' with begin/end" in {
    val text = """|module foo;
                  |  always @* begin
                  |    if (x) begin a = b; end
                  |    else if (y) begin b = a; end
                  |  end
                  |endmodule
                  |""".stripMargin
    val warnings = Warnings(BEGINEND)(Source("test.v", text))

    warnings should have length 0
  }

  it should "be detected for 'else' without begin/end" in {
    val text = """|module foo;
                  |  always @* begin
                  |    if (x) begin a = b; end
                  |    else b = a;
                  |  end
                  |endmodule
                  |""".stripMargin
    val warnings = Warnings(BEGINEND)(Source("test.v", text))

    warnings should have length 1

    warnings.head should be(BEGINEND(Loc("test.v", 4, 9), "else"))
  }

  it should "not be detected for 'else' with begin/end" in {
    val text = """|module foo;
                  |  always @* begin
                  |    if (x) begin a = b; end
                  |    else begin b = a; end
                  |  end
                  |endmodule
                  |""".stripMargin
    val warnings = Warnings(BEGINEND)(Source("test.v", text))

    warnings should have length 0
  }

  it should "be detected for 'for' without begin/end" in {
    val text = """|module foo;
                  |  always @* begin
                  |    for (i = 0 ; i < 2 ; i  = i + 1)
                  |      b = a;
                  |  end
                  |endmodule
                  |""".stripMargin
    val warnings = Warnings(BEGINEND)(Source("test.v", text))

    warnings should have length 1

    warnings.head should be(BEGINEND(Loc("test.v", 4, 6), "for"))
  }

  it should "not be detected for 'for' with begin/end" in {
    val text = """|module foo;
                  |  always @* begin
                  |    for (i = 0 ; i < 2 ; i  = i + 1) begin
                  |      b = a;
                  |    end
                  |  end
                  |endmodule
                  |""".stripMargin
    val warnings = Warnings(BEGINEND)(Source("test.v", text))

    warnings should have length 0
  }

  it should "be detected in example 1" in {
    val text = """|module foo;
                  |always @(posedge clk or negedge rst_n)
                  |if      (!rst_n)                 {p_controlb_data, p_controla_data} <= {2{32'b0}};
                  |else if (apb_setup && m2s_write) {p_controlb_data, p_controla_data} <= {2{m2s_wdata}};
                  |endmodule
                  |""".stripMargin
    val warnings = Warnings(BEGINEND)(Source("test.v", text))

    warnings should have length 3
  }

}

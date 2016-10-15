package com.argondesign.alint.warnings

import org.scalatest.FlatSpec
import org.scalatest.Matchers
import com.argondesign.alint.Source
import com.argondesign.alint.Loc
import com.argondesign.alint.warnings._

class ALWAYSASSIGNMENTSSpec extends FlatSpec with Matchers {

  "ALWAYSASSIGNMENTS" should "be detected in a malformed combinatorial always block" in {
    val text = """|module foo;
                  |  always @* begin
                  |    if (a) begin
                  |      b = 1;
                  |    end else begin
                  |      c <= 1;
                  |    end
                  |  end
                  |endmodule
                  |""".stripMargin
    val warnings = Warnings(ALWAYSASSIGNMENTS)(Source("test.v", text))

    warnings should have length 1

    warnings.head should be(ALWAYSASSIGNMENTS(Loc("test.v", 2, 2), 1))
  }

  it should "be detected in a malformed sequential always block" in {
    val text = """|module foo;
                  |  always @(posedge clk) begin
                  |    if (a) begin
                  |      b = 1;
                  |    end else begin
                  |      c <= 1;
                  |    end
                  |  end
                  |endmodule
                  |""".stripMargin
    val warnings = Warnings(ALWAYSASSIGNMENTS)(Source("test.v", text))

    warnings should have length 1

    warnings.head should be(ALWAYSASSIGNMENTS(Loc("test.v", 2, 2), 0))
  }

  it should "not be detected in a well formed combinatorial always block" in {
    val text = """|module foo;
                  |  always @* begin
                  |    b = 1;
                  |    c = 1;
                  |  end
                  |endmodule
                  |""".stripMargin
    val warnings = Warnings(ALWAYSASSIGNMENTS)(Source("test.v", text))

    warnings should have length 0
  }

  it should "not be detected in a well formed sequantial always block" in {
    val text = """|module foo;
                  |  always @(posedge clk) begin
                  |    b <= 1;
                  |    c <= 1;
                  |  end
                  |endmodule
                  |""".stripMargin
    val warnings = Warnings(ALWAYSASSIGNMENTS)(Source("test.v", text))

    warnings should have length 0
  }

  it should "not be detected in separate well formed always blocks" in {
    val text = """|module foo;
                  |  always @(posedge clk) begin
                  |    b <= 1;
                  |    c <= 1;
                  |  end
                  |  always @* begin
                  |    b = 1;
                  |    c = 1;
                  |  end
                  |endmodule
                  |""".stripMargin
    val warnings = Warnings(ALWAYSASSIGNMENTS)(Source("test.v", text))

    warnings should have length 0
  }
}

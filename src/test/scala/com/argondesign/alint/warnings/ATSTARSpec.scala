package com.argondesign.alint.warnings

import org.scalatest._
import com.argondesign.alint.Source
import com.argondesign.alint.Loc

class ATSTARSpec extends FlatSpec with Matchers {
  "ATSTAR" should "be detected for 'alwas @(*)'" in {
    val text = """|module foo;
                  |  always @(*) begin
                  |  end
                  |endmodule
                  |""".stripMargin
    val warnings = Warnings.collect[ATSTAR](Source("test.v", text))

    warnings should have length 1

    warnings.head should be(ATSTAR(Loc("test.v", 2, 2)))
  }

  it should "be detected for 'alwas @ ( * )'" in {
    val text = """|module foo;
                  |  always @ ( * ) begin
                  |  end
                  |endmodule
                  |""".stripMargin
    val warnings = Warnings.collect[ATSTAR](Source("test.v", text))

    warnings should have length 1

    warnings.head should be(ATSTAR(Loc("test.v", 2, 2)))
  }

  it should "not be detected for 'alwas @*'" in {
    val text = """|module foo;
                  |  always @* begin
                  |  end
                  |endmodule
                  |""".stripMargin
    val warnings = Warnings.collect[ATSTAR](Source("test.v", text))

    warnings should have length 0
  }

  it should "not be detected for 'alwas @(posedge clk)'" in {
    val text = """|module foo;
                  |  always @(posedge clk) begin
                  |  end
                  |endmodule
                  |""".stripMargin
    val warnings = Warnings.collect[ATSTAR](Source("test.v", text))

    warnings should have length 0
  }

  it should "not be detected for 'alwas #10'" in {
    val text = """|module foo;
                  |  always #10 begin
                  |  end
                  |endmodule
                  |""".stripMargin
    val warnings = Warnings.collect[ATSTAR](Source("test.v", text))

    warnings should have length 0
  }
}

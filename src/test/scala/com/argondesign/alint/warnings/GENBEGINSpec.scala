package com.argondesign.alint.warnings

import org.scalatest.FlatSpec
import org.scalatest.Matchers
import com.argondesign.alint.Source
import com.argondesign.alint.Loc

class GENBEGINESpec extends FlatSpec with Matchers {

  "GENBEGIN" should "not be detected in generate for with begin/end" in {
    val text = """|module foo;
                  |  for (a = 0 ; a ; a = a) begin
                  |    assign b = 0;
                  |  end
                  |endmodule
                  |""".stripMargin
    val warnings = Warnings(GENBEGIN)(Source("test.v", text))

    warnings should have length 0
  }

  it should "be detected in generate for without begin/end" in {
    val text = """|module foo;
                  |  for (a = 0 ; a ; a = a)
                  |    assign b = 0;
                  |endmodule
                  |""".stripMargin
    val warnings = Warnings(GENBEGIN)(Source("test.v", text))

    warnings should have length 1

    warnings.head should be(GENBEGIN(Loc("test.v", 3, 4)))
  }

  it should "not be detected in generate if with begin/end" in {
    val text = """|module foo;
                  |  if (a) begin
                  |    assign b = 0;
                  |  end
                  |endmodule
                  |""".stripMargin
    val warnings = Warnings(GENBEGIN)(Source("test.v", text))

    warnings should have length 0
  }

  it should "be detected in generate if without begin/end" in {
    val text = """|module foo;
                  |  if (a)
                  |    assign b = 0;
                  |endmodule
                  |""".stripMargin
    val warnings = Warnings(GENBEGIN)(Source("test.v", text))

    warnings should have length 1

    warnings.head should be(GENBEGIN(Loc("test.v", 3, 4)))
  }

  it should "not be detected in generate else with begin/end" in {
    val text = """|module foo;
                  |  if (a) begin
                  |    assign b = 0;
                  |  end else begin
                  |    assign b = 1;
                  |  end
                  |endmodule
                  |""".stripMargin
    val warnings = Warnings(GENBEGIN)(Source("test.v", text))

    warnings should have length 0
  }

  it should "be detected in generate else without begin/end" in {
    val text = """|module foo;
                  |  if (a) begin
                  |    assign b = 0;
                  |  end else
                  |    assign b = 1;
                  |endmodule
                  |""".stripMargin
    val warnings = Warnings(GENBEGIN)(Source("test.v", text))

    warnings should have length 1

    warnings.head should be(GENBEGIN(Loc("test.v", 5, 4)))
  }

  it should "not be detected in generate else if with begin/end" in {
    val text = """|module foo;
                  |  if (a) begin
                  |    assign b = 0;
                  |  end else if (b) begin
                  |    assign b = 1;
                  |  end
                  |endmodule
                  |""".stripMargin
    val warnings = Warnings(GENBEGIN)(Source("test.v", text))

    warnings should have length 0
  }

  it should "be detected in generate else if without begin/end" in {
    val text = """|module foo;
                  |  if (a) begin
                  |    assign b = 0;
                  |  end else if (b)
                  |    assign b = 1;
                  |endmodule
                  |""".stripMargin
    val warnings = Warnings(GENBEGIN)(Source("test.v", text))

    warnings should have length 1

    warnings.head should be(GENBEGIN(Loc("test.v", 5, 4)))
  }

  it should "be detected in generate blocks wihtout begin/end under generate else if" in {
    val text = """|module foo;
                  |  if (a) begin
                  |    assign b = 0;
                  |  end else if (b) begin
                  |    if (c)
                  |      assign b = 1;
                  |  end
                  |endmodule
                  |""".stripMargin
    val warnings = Warnings(GENBEGIN)(Source("test.v", text))

    warnings should have length 1

    warnings.head should be(GENBEGIN(Loc("test.v", 6, 6)))
  }

  it should "not be detected in generate case item with begin/end" in {
    val text = """|module foo;
                  |  case(a)
                  |    a: begin
                  |      assign b = 0;
                  |    end
                  |  endcase
                  |endmodule
                  |""".stripMargin
    val warnings = Warnings(GENBEGIN)(Source("test.v", text))

    warnings should have length 0
  }

  it should "be detected in generate case item without begin/end" in {
    val text = """|module foo;
                  |  case(a)
                  |    a: assign b = 0;
                  |  endcase
                  |endmodule
                  |""".stripMargin
    val warnings = Warnings(GENBEGIN)(Source("test.v", text))

    warnings should have length 1

    warnings.head should be(GENBEGIN(Loc("test.v", 3, 7)))
  }
}

package com.argondesign.alint.warnings

import org.scalatest._
import com.argondesign.alint.Source
import com.argondesign.alint.Loc

class ANSIMODULESpec extends FlatSpec with Matchers {
  "ANSIMODULE" should "be detected for non-ANSI style module declarations" in {
    val text = """|module foo (a, b);
                  |endmodule
                  |""".stripMargin
    val warnings = Warnings(ANSIMODULE)(Source("test.v", text))

    warnings should have length 1

    warnings.head should be(ANSIMODULE(Loc("test.v", 1, 7), "foo"))
  }

  it should "not be detected for ANSI style module declarations" in {
    val text = """|module foo(input a, output b);
                  |endmodule
                  |""".stripMargin
    val warnings = Warnings(ANSIMODULE)(Source("test.v", text))

    warnings should have length 0
  }
}

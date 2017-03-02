package com.argondesign.alint.warnings

import org.scalatest._
import com.argondesign.alint.Loc
import com.argondesign.alint.Source

class MODULEFILENAMESpec extends FlatSpec with Matchers {
  "MODULEFILENAME" should "be detected for non-ANSI module declarations not matching file name" in {
    val text = """|module foo (a, b);
                  |endmodule
                  |""".stripMargin
    val warnings = Warnings(MODULEFILENAME)(Source("test.v", text))

    warnings should have length 1

    warnings.head should be(MODULEFILENAME(Loc("test.v", 1, 7), "foo"))
  }

  it should "be detected for ANSI module declarations not matching file name" in {
    val text = """|module foo (input a, output b);
                  |endmodule
                  |""".stripMargin
    val warnings = Warnings(MODULEFILENAME)(Source("test.v", text))

    warnings should have length 1

    warnings.head should be(MODULEFILENAME(Loc("test.v", 1, 7), "foo"))
  }

  it should "not be detected for non-ANSI module declarations matching file name" in {
    val text = """|module foo(a, b);
                  |endmodule
                  |""".stripMargin
    val warnings = Warnings(MODULEFILENAME)(Source("foo.v", text))

    warnings should have length 0
  }

  it should "not be detected for ANSI module declarations matching file name" in {
    val text = """|module foo(input a, output b);
                  |endmodule
                  |""".stripMargin
    val warnings = Warnings(MODULEFILENAME)(Source("foo.v", text))

    warnings should have length 0
  }
}

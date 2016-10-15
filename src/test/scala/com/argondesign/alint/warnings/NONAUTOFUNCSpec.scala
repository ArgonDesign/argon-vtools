package com.argondesign.alint.warnings

import org.scalatest._
import com.argondesign.alint.Source
import com.argondesign.alint.Loc

class NONAUTOFUNCSpec extends FlatSpec with Matchers {
  "NONAUTOFUNC" should "be detected for non-automatic functions" in {
    val text = """|module foo;
                  |  function func(input a);
                  |    func = a;
                  |  endfunction
                  |endmodule
                  |""".stripMargin
    val warnings = Warnings(NONAUTOFUNC)(Source("test.v", text))

    warnings should have length 1

    warnings.head should be(NONAUTOFUNC(Loc("test.v", 2, 2), "func"))
  }

  it should "not be detected for automatic functions" in {
    val text = """|module foo;
                  |  function automatic func(input a);
                  |    func = a;
                  |  endfunction
                  |endmodule
                  |""".stripMargin
    val warnings = Warnings(NONAUTOFUNC)(Source("test.v", text))

    warnings should have length 0
  }
}

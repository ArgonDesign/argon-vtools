package com.argondesign.alint

import org.scalatest.Matchers
import org.scalatest.FlatSpec

class SytnaxErrorSpec extends FlatSpec with Matchers {

  val incorrectInput = """|module foo;
                          |  if (1) begin
                          |    assign a = 0;
                          |endmodule
                          |""".stripMargin

  "SyntaxErrorException" should "be thrown for incorrect input" in {
    a[SyntaxErrorException] should be thrownBy {
      Source("test.v", incorrectInput).parseTree
    }
  }

  it should "have loc set to the location of the error" in {
    val exception = the[SyntaxErrorException] thrownBy {
      Source("test.v", incorrectInput).parseTree
    }

    exception.error.loc should be(Loc("test.v", 4, 0))
  }
}

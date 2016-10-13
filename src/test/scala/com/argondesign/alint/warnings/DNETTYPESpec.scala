package com.argondesign.alint.warnings

import org.scalatest._
import com.argondesign.alint.Source
import com.argondesign.alint.warnings._
import com.argondesign.alint.Loc
import org.scalactic.Pass

class DNETTYPESpec extends FlatSpec with Matchers {

  "DNETTYPE" should "be detected when missing directive at beginning of the file" in {
    val text = """|// Missing `default_nettype none
                  |module foo;
                  |endmodule
                  |`default_nettype wire
                  |""".stripMargin
    val warnings = Warnings[DNETTYPE](Source("test.v", text))

    warnings should have length 1

    warnings.head should be(DNETTYPE(Loc("test.v", 2, 0), 0))
  }

  it should "be detected when missing directive at end of the file" in {
    val text = """|`default_nettype none
                  |module foo;
                  |endmodule
                  |// Missing `default_nettype wire
                  |""".stripMargin
    val warnings = Warnings[DNETTYPE](Source("test.v", text))

    warnings should have length 1

    warnings.head should be(DNETTYPE(Loc("test.v", 3, 0), 1))
  }

  it should "be detected when multiple directives at the beginning of the file" in {
    val text = """|`default_nettype none
                  |`default_nettype none // Multiple default_nettype
                  |module foo;
                  |endmodule
                  |`default_nettype wire
                  |""".stripMargin
    val warnings = Warnings[DNETTYPE](Source("test.v", text))

    warnings should have length 1

    warnings.head should be(DNETTYPE(Loc("test.v", 1, 0), 2))
  }

  it should "be detected when multiple directives at the end of the file" in {
    val text = """|`default_nettype none
                  |module foo;
                  |endmodule
                  |`default_nettype none // Multiple default_nettype
                  |`default_nettype wire
                  |""".stripMargin
    val warnings = Warnings[DNETTYPE](Source("test.v", text))

    warnings should have length 1

    warnings.head should be(DNETTYPE(Loc("test.v", 4, 0), 3))
  }

  it should "be detected when directive not at the beginning of the file" in {
    val text = """|`include "none" // default_nettype not first
                  |`default_nettype none
                  |module foo;
                  |endmodule
                  |`default_nettype wire
                  |""".stripMargin
    val warnings = Warnings[DNETTYPE](Source("test.v", text))

    warnings should have length 1

    warnings.head should be(DNETTYPE(Loc("test.v", 2, 0), 4))
  }

  it should "be detected when directive not at the end of the file" in {
    val text = """|`default_nettype none
                  |module foo;
                  |endmodule
                  |`default_nettype wire
                  |`include "none" // default_nettype not last
                  |""".stripMargin
    val warnings = Warnings[DNETTYPE](Source("test.v", text))

    warnings should have length 1

    warnings.head should be(DNETTYPE(Loc("test.v", 4, 0), 5))
  }

  it should "be detected when directive with wrong parameter at the beginning of the file" in {
    val text = """|`default_nettype wire
                  |module foo;
                  |endmodule
                  |`default_nettype wire
                  |""".stripMargin
    val warnings = Warnings[DNETTYPE](Source("test.v", text))

    warnings should have length 1

    warnings.head should be(DNETTYPE(Loc("test.v", 1, 0), 6))
  }

  it should "be detected when directive with wrong parameter at the end of the file" in {
    val text = """|`default_nettype none
                  |module foo;
                  |endmodule
                  |`default_nettype none
                  |""".stripMargin
    val warnings = Warnings[DNETTYPE](Source("test.v", text))

    warnings should have length 1

    warnings.head should be(DNETTYPE(Loc("test.v", 4, 0), 7))
  }

  it should "not be detected in a well formed file" in {
    val text = """|`default_nettype none
                  |module foo;
                  |endmodule
                  |`default_nettype wire
                  |""".stripMargin
    val warnings = Warnings[DNETTYPE](Source("test.v", text))

    warnings should have length 0
  }
}

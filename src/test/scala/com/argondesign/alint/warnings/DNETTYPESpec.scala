package com.argondesign.alint.warnings

import org.scalatest._
import com.argondesign.alint.Source
import com.argondesign.alint.warnings._
import com.argondesign.alint.Loc
import org.scalactic.Pass

class DNETTYPESpec extends FlatSpec with Matchers {

  "DNETTYPE1A" should "be detected" in {
    val text = """|// Missing `default_nettype none
                  |module foo;
                  |endmodule
                  |`default_nettype wire
                  |""".stripMargin
    val warnings = Warnings[DNETTYPE](Source("test.v", text))

    warnings should have length 1

    warnings.head should be(DNETTYPE1A(Loc("test.v", 2, 0)))
  }

  "DNETTYPE1B" should "be detected" in {
    val text = """|`default_nettype none
                  |module foo;
                  |endmodule
                  |// Missing `default_nettype wire
                  |""".stripMargin
    val warnings = Warnings[DNETTYPE](Source("test.v", text))

    warnings should have length 1

    warnings.head should be(DNETTYPE1B(Loc("test.v", 3, 0)))
  }

  "DNETTYPE2A" should "be detected" in {
    val text = """|`default_nettype none
                  |`default_nettype none // Multiple default_nettype
                  |module foo;
                  |endmodule
                  |`default_nettype wire
                  |""".stripMargin
    val warnings = Warnings[DNETTYPE](Source("test.v", text))

    warnings should have length 1

    warnings.head should be(DNETTYPE2A(Loc("test.v", 1, 0)))
  }

  "DNETTYPE2B" should "be detected" in {
    val text = """|`default_nettype none
                  |module foo;
                  |endmodule
                  |`default_nettype none // Multiple default_nettype
                  |`default_nettype wire
                  |""".stripMargin
    val warnings = Warnings[DNETTYPE](Source("test.v", text))

    warnings should have length 1

    warnings.head should be(DNETTYPE2B(Loc("test.v", 4, 0)))
  }

  "DNETTYPE3A" should "be detected" in {
    val text = """|`include "none" // default_nettype not first
                  |`default_nettype none
                  |module foo;
                  |endmodule
                  |`default_nettype wire
                  |""".stripMargin
    val warnings = Warnings[DNETTYPE](Source("test.v", text))

    warnings should have length 1

    warnings.head should be(DNETTYPE3A(Loc("test.v", 2, 0)))
  }

  "DNETTYPE3B" should "be detected" in {
    val text = """|`default_nettype none
                  |module foo;
                  |endmodule
                  |`default_nettype wire
                  |`include "none" // default_nettype not last
                  |""".stripMargin
    val warnings = Warnings[DNETTYPE](Source("test.v", text))

    warnings should have length 1

    warnings.head should be(DNETTYPE3B(Loc("test.v", 4, 0)))
  }

  "DNETTYPE4A" should "be detected" in {
    val text = """|`default_nettype wire
                  |module foo;
                  |endmodule
                  |`default_nettype wire
                  |""".stripMargin
    val warnings = Warnings[DNETTYPE](Source("test.v", text))

    warnings should have length 1

    warnings.head should be(DNETTYPE4A(Loc("test.v", 1, 0)))
  }

  "DNETTYPE4B" should "be detected" in {
    val text = """|`default_nettype none
                  |module foo;
                  |endmodule
                  |`default_nettype none
                  |""".stripMargin
    val warnings = Warnings[DNETTYPE](Source("test.v", text))

    warnings should have length 1

    warnings.head should be(DNETTYPE4B(Loc("test.v", 4, 0)))
  }

  "DNETTYPE" should "not be detected" in {
    val text = """|`default_nettype none
                  |module foo;
                  |endmodule
                  |`default_nettype wire
                  |""".stripMargin
    val warnings = Warnings[DNETTYPE](Source("test.v", text))

    warnings should have length 0
  }
}

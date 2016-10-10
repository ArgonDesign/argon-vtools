package com.argondesign.alint

import org.antlr.v4.runtime.ParserRuleContext

class AnyVVisitor extends VVisitor[Boolean](false, (_ || _)) {
}

package com.argondesign.alint

class WarningsVisitor[T <: Warnings.Warning] extends VVisitor[List[T]](Nil, (_ ::: _)) {}

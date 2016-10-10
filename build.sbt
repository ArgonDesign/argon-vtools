////////////////////////////////////////////////////////////////////////////////
// General
////////////////////////////////////////////////////////////////////////////////

name := "alint"

organization := "com.argondesign"

version := "1"

scalaVersion := "2.11.8"

////////////////////////////////////////////////////////////////////////////////
// Antlr4 plugin
////////////////////////////////////////////////////////////////////////////////

antlr4Settings

antlr4PackageName in Antlr4 := Some("com.argondesign.alint.antlr4")

antlr4GenListener in Antlr4 := true

antlr4GenVisitor in Antlr4 := true

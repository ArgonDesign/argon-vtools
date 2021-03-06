////////////////////////////////////////////////////////////////////////////////
// Argon Design Ltd. Project P9000 Argon
// Copyright (c) 2016-2018 Argon Design Ltd. All rights reserved.
//
// This file is covered by the BSD (with attribution) license.
// See the LICENSE file for the precise wording of the license.
//
// Module : Argon Verilog Tools
// Author : Geza Lore
//
// DESCRIPTION:
//
// sbt configuration file
////////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////////////
// General
////////////////////////////////////////////////////////////////////////////////

name := "argon-vtools"

organization := "Argon Design"

version := "1.0"

scalaVersion := "2.11.8"

scalacOptions ++= Seq("-deprecation", "-feature", "-Xlint:_")

libraryDependencies += "org.scala-lang" % "scala-reflect" % scalaVersion.value
libraryDependencies += "org.scala-graph" %% "graph-core" % "1.11.4"
libraryDependencies += "com.github.scala-incubator.io" %% "scala-io-core" % "0.4.3"
libraryDependencies += "com.github.scala-incubator.io" %% "scala-io-file" % "0.4.3"
libraryDependencies += "org.rogach" %% "scallop" % "2.1.1"

////////////////////////////////////////////////////////////////////////////////
// Antlr4 plugin
////////////////////////////////////////////////////////////////////////////////

antlr4Settings

antlr4PackageName in Antlr4 := Some("com.argondesign.vtools.antlr4")

antlr4GenListener in Antlr4 := true

antlr4GenVisitor in Antlr4 := true

////////////////////////////////////////////////////////////////////////////////
// Antlr4 postprocessing
////////////////////////////////////////////////////////////////////////////////

val antlr4PostprocessDir = SettingKey[File]("Directory to place Antlr4 postprocessor output")
antlr4PostprocessDir := (sourceManaged in Compile).value / "antlr4Post"
managedSourceDirectories in Compile <+= antlr4PostprocessDir
cleanFiles <+= antlr4PostprocessDir

val antlr4Postprocess = TaskKey[Seq[File]]("Derive further sources from Antlr4 output")
antlr4Postprocess := {
  val parsers = (antlr4Generate in Antlr4).value.filter(_.name matches ".*Parser.java")
  val pkg = (antlr4PackageName in Antlr4).value.getOrElse("")
  val pkgDir = pkg.replaceAll("\\.", "/")

  var ruleContextsTraits: List[sbt.File] = Nil
  for (parser <- parsers) {
    val parserName = parser.name.takeWhile(_ != '.')
    val ruleContextsTrait = antlr4PostprocessDir.value / pkgDir / s"${parserName}RuleContexts.scala"
    val ruleContextsTraitText = new StringBuilder()

    ruleContextsTraitText ++= (pkg match {
      case "" => ""
      case _ => s"package $pkg\n\n"
    })

    ruleContextsTraitText ++= s"trait ${parserName}RuleContexts {\n"

    for (line <- scala.io.Source.fromFile(parser).getLines()) {
      val pattern = """.*public static class (.*) extends \w+Context .*""".r
      line match {
        case pattern(name) => ruleContextsTraitText ++= s"  type $name = $parserName.$name\n"
        case _ => ;
      }
    }

    ruleContextsTraitText ++= "}\n"

    IO.write(ruleContextsTrait, ruleContextsTraitText.toString)
    ruleContextsTraits = ruleContextsTrait :: ruleContextsTraits
  }
  ruleContextsTraits
}
sourceGenerators in Compile <+= antlr4Postprocess

////////////////////////////////////////////////////////////////////////////////
// ScalaTest
////////////////////////////////////////////////////////////////////////////////

libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.0" % "test"

logBuffered in Test := false

////////////////////////////////////////////////////////////////////////////////
// SBT native packager
////////////////////////////////////////////////////////////////////////////////

enablePlugins(JavaAppPackaging)

stage := (stage dependsOn (test in Test)).value

// Prepend '--' to the command line arguments in the wrapper script.
// This in fact causes the wrapper script to not consume any arguments,
// in particular -D options
bashScriptExtraDefines += """set -- -- "$@""""

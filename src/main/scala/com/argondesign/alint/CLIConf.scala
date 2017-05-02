package com.argondesign.alint

import org.rogach.scallop.singleArgConverter
import org.rogach.scallop.listArgConverter
import org.rogach.scallop.ScallopConf
import org.rogach.scallop.Subcommand
import scalax.file.Path
import org.rogach.scallop.exceptions.ValidationFailure

class CLIConf(args: Seq[String]) extends ScallopConf(args) {
  implicit val pathCovnert = singleArgConverter[Path](Path.fromString(_))
  implicit val sourceListConverter = listArgConverter[Source](Source(_))

  printedName = "alint"

  banner("Process Verilog files")

  def sourceListValidator(v: List[Source]): Either[String, Unit] = {
    val messages = for (source <- v if source.path.nonExistent) yield {
      s"Input file '${source.path.path}' does not exist."
    }

    if (messages.isEmpty) Right(Unit) else Left(messages mkString ("\n", "\n", ""))
  }

  object LintSubcommand extends Subcommand("lint") {
    banner("Lint input files")
    footer("")

    val sources = trailArg[List[Source]](descr = "Input files")
    validate(sources)(sourceListValidator)
  }
  val lint = LintSubcommand
  addSubcommand(lint)

  object MangleSubcommand extends Subcommand("mangle") {
    banner("Obfuscate input files, preserving only the interfaces on top level modules")
    footer("")

    val odir = opt[Path](
      noshort = true, required = true,
      descr = "Output directory to write mangled source files to")

    val salt = opt[String](
      default = Some(System.nanoTime.toString), noshort = true,
      descr = "Salt to add to mangling")

    val map = opt[Path](
      noshort = true, required = false,
      descr = "Name of inverse name map file.")

    val sources = trailArg[List[Source]](descr = "Input files")
    validate(sources)(sourceListValidator)
  }
  val mangle = MangleSubcommand
  addSubcommand(mangle)

  verify()
}

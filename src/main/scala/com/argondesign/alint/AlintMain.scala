/*
 * Copyright 2016.
 */

package com.argondesign.alint

import org.antlr.v4.runtime._
import com.argondesign.alint.antlr4._
import scala.collection.JavaConversions._

object AlintMain extends App {
//  println("Hello there!")
//  println(System.getenv("PWD"))
//  println(args(0))
  
  val inputFile = args(0)
  
  val inputStream = new ANTLRFileStream(inputFile)
  val lexer = new VLexer(inputStream)
  val tokenStream = new CommonTokenStream(lexer)
  val parser = new VParser(tokenStream)
  val tree = parser.start()
//  println(tree.toStringTree(parser))
  
//  tokenStream.fill() 
//  
//  tokenStream.getTokens foreach {
//    println(_)
//  }
}  
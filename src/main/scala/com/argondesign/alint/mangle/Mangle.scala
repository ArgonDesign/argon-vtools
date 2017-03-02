package com.argondesign.alint.mangle

import scala.collection.convert.WrapAsJava
import scala.collection.convert.WrapAsScala
import com.argondesign.alint.Antlr4Conversions._
import com.argondesign.alint.Source
import com.argondesign.alint.Hierarchy
import com.argondesign.alint.Visitor
import org.antlr.v4.runtime.Token
import com.argondesign.alint.antlr4.VParser

object Mangle {
  object ParameterNames extends Visitor[Set[String]](Set(), (_ union _)) {
    override def visitParamAssignment(ctx: ParamAssignmentContext) = Set(ctx.IDENTIFIER.text)
  }

  object PortNames extends Visitor[Set[String]](Set(), (_ union _)) {
    override def visitListOfIds(ctx: ListOfIdsContext) = {
      (ctx.IDENTIFIER map { _.text }).toSet
    }
    override def visitListOfVariablePortIdentifiers(ctx: ListOfVariablePortIdentifiersContext) = {
      (ctx.IDENTIFIER map { _.text }).toSet
    }
  }

  object InterfaceNames extends Visitor[Set[String]](Set(), (_ union _)) {
    override def visitParameterDeclaration(ctx: ParameterDeclarationContext) = ParameterNames(ctx.listOfParamAssignments)
    override def visitPortDeclaration(ctx: PortDeclarationContext) = PortNames(ctx)
    override def visitModuleDeclarationAnsi(ctx: ModuleDeclarationAnsiContext) = {
      Set(ctx.IDENTIFIER.text) union visitChildren(ctx)
    }
  }

  object PreservedTokens extends Visitor[Set[Token]](Set(), (_ union _)) {
    override def visitDefaultNettypeDirective(ctx: DefaultNettypeDirectiveContext) = {
      if (ctx.IDENTIFIER != null) Set(ctx.IDENTIFIER) else Set()
    }
  }

  object MangledTokens extends Visitor[Set[Token]](Set(), (_ union _)) {
    override def visitNamedParameterAssignment(ctx: NamedParameterAssignmentContext) = Set(ctx.IDENTIFIER)
    override def visitNamedPortConnection(ctx: NamedPortConnectionContext) = Set(ctx.IDENTIFIER)
  }

  def hash(s: String): String = {
    val md5Buf = java.security.MessageDigest.getInstance("MD5").digest(s.getBytes)
    val md5Str = md5Buf map { "%02x" format _ } mkString ""
    (md5Str + "x" + md5Str) dropWhile { '0' to '9' contains _ } take 16
  }

  def apply(sources: List[Source]): List[Source] = {
    val hierarchy = Hierarchy(sources)
    val topLevelModules = hierarchy.nodes filter { _.inDegree == 0 } map { _.toOuter }

    for (source <- sources) yield {
      val baseName = source.name.split("/").reverse.head
      val (pre, suf) = baseName span { _ != '.' }

      val isTopLevel = topLevelModules contains pre

      val newName = if (isTopLevel) pre + suf else hash(pre) + suf

      val newText = {
        val identifierTokens = source.tokens.toSet filter { _.getType == VParser.IDENTIFIER }
        val candidateTokens = if (isTopLevel) {
          val interfaceNames = InterfaceNames(source)
          identifierTokens filterNot { interfaceNames contains _.text }
        } else {
          identifierTokens
        }

        val mangledTokens = candidateTokens diff PreservedTokens(source) union MangledTokens(source)

        val parts = for (token <- source.tokens if token.getType != Token.EOF) yield {
          if (mangledTokens contains token) {
            hash(token.text)
          } else if (!token.isHidden) {
            token.text
          } else {
            token.text match {
              case s if s matches "/\\*.*verilator.*lint.*\\*/" => token.text
              case s if s contains '\n' => "\n"
              case _ => " "
            }
          }
        }

        parts mkString "" replaceAll ("\\s*\n\\s*", "\n") dropWhile { _ == '\n' }
      }

      Source(newName, newText)
    }
  }
}

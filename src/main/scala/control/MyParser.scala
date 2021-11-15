package de.htwg.rs.chatbot
package control

import de.htwg.rs.chatbot.model.Command

import scala.util.Success
import scala.util.parsing.combinator.RegexParsers

class RuleParser extends RegexParsers {
  def command =
    """(starts with|contains|is|ends with)""".r ^^ {
      _.toString
    }

  def commandParameter =
    """\".*?\"""".r ^^ {
      _.toString.drop(1).dropRight(1)
    }

  def sequence = "when message" ~ command ~ commandParameter ~ "respond with" ~ commandParameter ^^ {
    case _ ~ command ~ commandParameter1 ~ _ ~ commandParameter2
    => command match
      case "starts with" => `when message` `starts with` commandParameter1 `respond with` commandParameter2
      case "contains" => `when message` contains commandParameter1 `respond with` commandParameter2
      case "is" => `when message` is commandParameter1 `respond with` commandParameter2
      case "ends with" => `when message` `ends with` commandParameter1 `respond with` commandParameter2
  }
}


class MyParser extends RuleParser {
  def apply(text: String): Either[String, Command] = {
    parse(sequence, text) match {
      case Success(t, _) => Right(t)
      case NoSuccess(msg, next) => Left("Oh neim! hat nix geklapt :(")
    }
  }
}
package de.htwg.rs.chatbot
package control

import model.Command

import scala.util.parsing.combinator.RegexParsers

class RuleParser extends RegexParsers :
  def sequence = "when message" ~ command ~ commandParameter ~ "respond with" ~ commandParameter ^^ {
    case _ ~ command ~ commandParameter1 ~ _ ~ commandParameter2
    => command match
      case "starts with" => `when message` `starts with` commandParameter1 `respond with` commandParameter2
      case "contains" => `when message` contains commandParameter1 `respond with` commandParameter2
      case "is" => `when message` is commandParameter1 `respond with` commandParameter2
      case "ends with" => `when message` `ends with` commandParameter1 `respond with` commandParameter2
  }

  def command =
    """(starts with|contains|is|ends with)""".r ^^ {
      _.toString
    }

  def commandParameter =
    """\".*?\"""".r ^^ {
      _.toString.drop(1).dropRight(1)
    }


class CommandRuleParser extends RuleParser :
  def apply(text: String): Either[String, Command] =
    parse(sequence, text) match
      case Success(t, _) => Right(t)
      case NoSuccess(msg, next) => Left("Parsing not successful!")

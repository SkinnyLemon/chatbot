package de.htwg.rs.chatbot
package control

import scala.util.Success
import scala.util.parsing.combinator.RegexParsers

class RuleParser extends RegexParsers {
  def command =
    """(starts with|contains|is|ends with)""".r ^^ {
      _.toString
    }

  def commandParameter =
    """\".*?\"""".r ^^ {
      _.toString
    }

  def sequence = "when message" ~ command ~ commandParameter ~ "respond with" ~ commandParameter ^^ { case _ ~ command ~ commandParameter1 ~ _ ~ commandParameter2
  => command match
    case "starts with" => `when message` `starts with`  commandParameter1 `respond with` commandParameter2
    case "contains" => `when message` contains commandParameter1 `respond with` commandParameter2
    case "is" => `when message` is commandParameter1 `respond with` commandParameter2
    case "ends with" => `when message` `ends with` commandParameter1 `respond with` commandParameter2
  }
}


class Parser extends RuleParser {

  val commandMessage = "when message starts with \"howdy\" respond with \"partner\""

  def parsed = parse(sequence, commandMessage) match {
    case Success(t, _) => Right(t)
    case NoSuccess(msg, next) => 
      Left("Oh neim! hat nix geklapt :(")
  }


}
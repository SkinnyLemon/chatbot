package de.htwg.rs.chatbot
package control

import model.{Command, TwitchInput}

import de.htwg.rs.chatbot.control.MyParser

import scala.util.Success

class CommandParser() {
  val commandPrefix = "!create"

  def handle(input: TwitchInput): (Option[Command], Option[String]) =
    if (!input.message.text.startsWith(commandPrefix))
      return (None, None)

    val parse = new MyParser()
    parse(input.message.text.drop(commandPrefix.length)) match
      case Right(cmd) =>
        (Some(cmd), Some("command created"))
      case Left(value) =>
        (None, Some("failed to parse command"))
}

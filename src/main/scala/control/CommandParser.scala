package de.htwg.rs.chatbot
package control

import model.{Command, TwitchInput}

class CommandParser:
  val commandPrefix = "!create"

  def handle(input: TwitchInput): (Option[Command], Option[String]) =
    if (!input.message.text.startsWith(commandPrefix))
      return (None, None)
    val parse = new CommandRuleParser()
    parse(input.message.text.drop(commandPrefix.length)) match
      case Right(cmd) =>
        (Some(cmd), Some("command created"))
      case Left(_) =>
        (None, Some("failed to parse command"))

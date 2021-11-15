package de.htwg.rs.chatbot
package control

import model.{Command, TwitchInput}

import de.htwg.rs.chatbot.control.MyParser

import scala.util.Success

case class CreateCommandHandler(registries: CommandRegistryRegisty) extends Command {
  override def handle(input: TwitchInput): (Command, Option[String]) =
    val parse = new MyParser()

    parse.apply(input.message.text) match
      case Right(cmd) =>
        registries.addCommand(input.channel.name, cmd)
        (cmd, Some("command created"))
      case Left(value) =>
        (copy(registries), None)

}

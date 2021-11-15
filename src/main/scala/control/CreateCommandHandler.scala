package de.htwg.rs.chatbot
package control

import model.{Command, TwitchInput}

import de.htwg.rs.chatbot.control.MyParser

import scala.util.Success

case class CreateCommandHandler(commands: List[Command] = List.empty) extends Command {
  override def handle(input: TwitchInput): (Command, Option[String]) =
    val parse = new MyParser()

    commands.foreach(println)
    commands.map(_.handle(input))
    parse(input.message.text) match
      case Right(cmd) =>
        (copy(commands:+ cmd), Some("command created"))
      case Left(value) =>
        (copy(commands), None)

}

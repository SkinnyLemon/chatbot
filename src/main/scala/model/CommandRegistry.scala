package de.htwg.rs.chatbot
package model

import control.{ChannelOutput, CommandParser}

case class CommandRegistry(output: ChannelOutput, commands: List[Command] = List.empty) {
  private val parser = new CommandParser()

  def addCommand(command: Command): CommandRegistry = copy(
    commands = commands :+ command)

  def removeCommand(command: Command): CommandRegistry = copy(
    commands = commands.filter(_ != command))

  def handleMessage(message: TwitchInput): CommandRegistry = {
    var newCommands = commands.map(command => {
      val (newCommand: Command, response: Option[String]) =
      command.handle(message)
      response.foreach(output.send(_))
      newCommand
    })
    parser.handle(message) match {
      case (Some(newCommand), Some(response)) =>
        output.send(response)
        newCommands = newCommands :+ newCommand
      case (None, Some(response)) => output.send(response)
      case (None, None) =>
        //TODO remove. Not testable
      case _ => new IllegalStateException().printStackTrace()
    }
    copy(commands = newCommands)
  }
}

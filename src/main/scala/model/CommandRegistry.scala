package de.htwg.rs.chatbot
package model

import game.CoinFlipGameHandler

import de.htwg.rs.chatbot.control.ChannelOutput

case class CommandRegistry(output: ChannelOutput, commands: List[Command] = List.empty) {
  def addCommand(command: Command): CommandRegistry = copy(
    commands = commands :+ command)

  def removeCommand(command: Command): CommandRegistry = copy(
    commands = commands.filter(_ != command))

  def handleMessage(message: TwitchInput): CommandRegistry = copy(
    commands = commands.map(command => {
      val (newCommand: Command, response: Option[String]) = command.handle(message)
      response.foreach(output.send(_))
      newCommand
    }))
}

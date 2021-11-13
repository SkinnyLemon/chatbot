package de.htwg.rs.chatbot
package view

import control.{ChannelOutput, CommandRegistryRegisty, TwitchInputProvider, `when message`}
import game.CoinFlipGameHandler
import io.{Config, TwitchConnection}

object ChatBot {
  def main(args: Array[String]): Unit = {
    val bot = Config.bots.head
    val twitchConnection = TwitchConnection.establishConnection(bot.name, bot.auth)
    twitchConnection.start()
    val twitchInput = twitchConnection.getInput
    val twitchOutput = twitchConnection.getOutput

    val heyCommand = `when message` `starts with` "hey" `respond with` "TheIlluminati"
    val helpCommand = `when message` `contains` "help" `respond with` "Commands: hello, help, play"
    val starWars = `when message` `ends with` "hello there" `respond with` "General Kenobi BrainSlug"
    val helloCommand = `when message` `is` "hello" `respond with` "Hello there! HeyGuys HeyGuys"

    val registries = new CommandRegistryRegisty(twitchOutput)
    registries.addCommand("imperiabot", heyCommand)
    registries.addCommand("imperiabot", helpCommand)
    registries.addCommand("imperiabot", starWars)
    registries.addCommand("imperiabot", helloCommand)
    registries.addCommand("imperiabot", new CoinFlipGameHandler())

    twitchInput.subscribe(new TwitchInputProvider(twitchOutput, twitchConnection, registries))
    args.foreach(twitchConnection.join(_))
  }
}

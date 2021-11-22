package de.htwg.rs.chatbot
package view

import control.{ChannelOutput, CommandRegistryRegisty, TwitchInputProvider, `when message`}
import game.CoinFlipGameHandler
import io.{Config, TwitchConnection}
import de.htwg.rs.chatbot.control._

object ChatBot {
  def main(args: Array[String]): Unit = {

    val bot = Config.bots.head
    val twitchConnection = TwitchConnection.establishConnection(bot.name, bot.auth)
    twitchConnection.start()
    val twitchInput = twitchConnection.getInput
    val twitchOutput = twitchConnection.getOutput

    val registries = new CommandRegistryRegisty(twitchOutput)

    twitchInput.subscribe(new TwitchInputProvider(twitchOutput, twitchConnection, registries))

    args.foreach(twitchConnection.join(_))


    val heyCommand = `when message` `starts with` "hey" `respond with` "TheIlluminati"
    val helpCommand = `when message` `contains` "help" `respond with` "Commands: hello, help, play"
    val starWars = `when message` `ends with` "hello there" `respond with` "General Kenobi BrainSlug"
    val helloCommand = `when message` `is` "hello" `respond with` "Hello there! HeyGuys HeyGuys"


    registries.addCommand("imperiabot", heyCommand)
    registries.addCommand("imperiabot", helpCommand)
    registries.addCommand("imperiabot", helloCommand)
    registries.addCommand("imperiabot", new CoinFlipGameHandler())
    registries.addCommand("imperiabot", starWars)
  }
}

package de.htwg.rs.chatbot
package view

import control.TwitchInputProvider
import io.{Config, TwitchConnection}

object ChatBot {
  def main(args: Array[String]): Unit = {
    val bot = Config.bots.head
    val twitchConnection = TwitchConnection.establishConnection(bot.name, bot.auth)
    twitchConnection.start()
    val twitchInput = twitchConnection.getInput
    val twitchOutput = twitchConnection.getOutput
    twitchInput.subscribe(new TwitchInputProvider(twitchOutput, twitchConnection))
    args.foreach(twitchConnection.join(_))
  }
}

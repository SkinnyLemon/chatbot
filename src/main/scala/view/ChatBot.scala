package de.htwg.rs.chatbot
package view

import control.TwitchInputProvider
import io.{Config, TwitchConnection}

object ChatBot {
  def main(args: Array[String]): Unit = {
    val bot = Config.bots.head
    val twitchConnection = TwitchConnection.establishConnection(bot.name, bot.auth)
    val twitchInput = twitchConnection.getInput
    twitchInput.subscribe(new TwitchInputProvider())
    args.foreach(twitchConnection.join(_))
    twitchConnection.run()
  }
}

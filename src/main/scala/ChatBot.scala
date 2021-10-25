package de.htwg.rs.chatbot

import service.TwitchConsumerImpl

import scala.util.Try

object ChatBot {
  def main(args: Array[String]): Unit = {
    val bot = Config.bots.head
    val twitchConnection = TwitchConnection.establishConnection(bot.name, bot.auth)
    val twitchInput = twitchConnection.getInput
    twitchInput.subscribe(new TwitchConsumerImpl())
    args.foreach(twitchConnection.join(_))
    twitchConnection.run()
  }
}

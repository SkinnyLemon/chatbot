package de.htwg.rs.chatbot

object ChatBot {
  def main(args: Array[String]): Unit = {
    val bot = Config.bots.head
    val twitchConnection = TwitchConnection.establishConnection(bot.name, bot.auth)
    val twitchInput = twitchConnection.getInput
    twitchInput.subscribe(new TwitchConsumer {
      override def onMessage(message: String): Unit = println(message)
    })
    args.foreach(twitchConnection.join(_))
    twitchConnection.run()
  }
}

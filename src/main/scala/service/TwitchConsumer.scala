package de.htwg.rs.chatbot.service
import de.htwg.rs.chatbot.TwitchConsumer

import scala.util.{Failure, Success, Try}

class TwitchConsumerImpl extends TwitchConsumer {
  val parser = new TwitchInputParser()

  override def onMessage(message: String): Unit = {
    //println(message)
    if (message.contains("PRIVMSG"))
      val twitchInput = parser.parseToTwitchInput(message) match {
        case Success(twitchInput) =>
          println(twitchInput)
        case Failure(error) =>
          error.printStackTrace()
      }
  }
}

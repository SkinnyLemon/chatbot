package de.htwg.rs.chatbot
package control

import de.htwg.rs.chatbot.io.TwitchConsumer
import de.htwg.rs.chatbot.model.TwitchInputParser

import scala.util.{Failure, Success, Try}

class TwitchInputProvider extends TwitchConsumer {
  val parser = new TwitchInputParser()
  private var messages = 0

  override def onMessage(message: String): Unit = {
    //println(message)
    if (message.contains("PRIVMSG"))
      val twitchInput = parser.parseToTwitchInput(message) match {
        case Success(twitchInput) =>
          println(twitchInput)
          println(messages)
          messages += 1
        case Failure(error) =>
          error.printStackTrace()
      }
  }
}

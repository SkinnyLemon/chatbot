package de.htwg.rs.chatbot
package control

import io.{TwitchConsumer, TwitchOutput}
import model.TwitchInputParser

import scala.util.{Failure, Success, Try}

class TwitchInputProvider(output: TwitchOutput) extends TwitchConsumer {
  val parser = new TwitchInputParser()

  override def onMessage(message: String): Unit = {
    if (message.contains("PRIVMSG"))
      val twitchInput = parser.parseToTwitchInput(message) match {
        case Success(twitchInput) =>
          println(twitchInput)
          output.sendMessage(
            "imperiabot",
            s"${twitchInput.message.text} (${twitchInput.message.emotes.length})",
            Map("reply-parent-msg-id" -> twitchInput.message.id))
        case Failure(error) =>
          error.printStackTrace()
      }
  }
}

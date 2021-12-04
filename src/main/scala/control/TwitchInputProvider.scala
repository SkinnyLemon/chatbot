package de.htwg.rs.chatbot
package control

import io.{TwitchConnection, TwitchConsumer, TwitchOutput}
import model.{TwitchInput, TwitchInputParser}

import scala.collection.mutable
import scala.util.{Failure, Success, Try}

class TwitchInputProvider(output: TwitchOutput, twitchConnection: TwitchConnection, commandRegistries: CommandRegistryRegisty) extends TwitchConsumer {
  private val parser = new TwitchInputParser()

  override def onMessage(message: String): Unit =
    if (message.contains("PRIVMSG"))
      parser.parseToTwitchInput(message) match {
        case Success(twitchInput) => commandRegistries.handleMessage(twitchInput)
        case Failure(error) => error.printStackTrace()
      }
}

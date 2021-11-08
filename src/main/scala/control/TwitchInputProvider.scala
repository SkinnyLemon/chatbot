package de.htwg.rs.chatbot
package control

import io.{TwitchConnection, TwitchConsumer, TwitchOutput}
import model.{TwitchInput, TwitchInputParser}

import de.htwg.rs.chatbot.game.*

import scala.collection.mutable
import scala.util.{Failure, Success, Try}

class TwitchInputProvider(output: TwitchOutput, twitchConnection: TwitchConnection) extends TwitchConsumer {
  val parser = new TwitchInputParser()
  
  private val channelGateways = mutable.Map.empty[String, ChannelGateway] 

  override def onMessage(message: String): Unit =
    if (message.contains("PRIVMSG"))
      val twitchInput = parser.parseToTwitchInput(message) match {
        case Success(twitchInput) => Try(channelGateways(twitchInput.channel.name)) match {
          case Success(gateway) => gateway.onMessage(twitchInput)
          case Failure(_) =>
            val gateway = new ChannelGateway(output, twitchInput.channel.name)
            channelGateways(twitchInput.channel.name) = gateway
            gateway.onMessage(twitchInput)
        }
        case Failure(error) =>
          error.printStackTrace()
      }


}


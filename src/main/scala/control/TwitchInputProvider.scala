package de.htwg.rs.chatbot
package control

import game.*
import io.{TwitchConnection, TwitchConsumer, TwitchOutput}
import model.{TwitchInput, TwitchInputParser}

import scala.collection.mutable
import scala.util.{Failure, Success, Try}

class TwitchInputProvider(output: TwitchOutput, twitchConnection: TwitchConnection) extends TwitchConsumer {
  private val parser = new TwitchInputParser()
  private val inputDistributor = new TwitchInputDistributor(output)

  override def onMessage(message: String): Unit =
    if (message.contains("PRIVMSG"))
      parser.parseToTwitchInput(message) match {
        case Success(twitchInput) => inputDistributor.distributeMessage(twitchInput)
        case Failure(error) => error.printStackTrace()
      }
}

class TwitchInputDistributor(output: TwitchOutput) {
  private val channelGateways = mutable.Map.empty[String, ChannelGateway]

  def subscribeTo(channelName: String, subscriber: Subscriber[TwitchInput]): Unit = getGateway(channelName).subscribe(subscriber)

  def unsubscribeFrom(channelName: String, subscriber: Subscriber[TwitchInput]): Unit = getGateway(channelName).unsubscribe(subscriber)
  
  def distributeMessage(message: TwitchInput): Unit = {
    val gateway = getGateway(message.channel.name)
    gateway.onMessage(message)
  }

  private def getGateway(channelName: String): ChannelGateway =
    channelGateways.get(channelName) match {
      case Some(gateway) => gateway
      case None => addGateway(channelName)
    }

  private def addGateway(channelName: String): ChannelGateway = {
    val gateway = new ChannelGateway(output, channelName)
    channelGateways(channelName) = gateway
    gateway
  }
}
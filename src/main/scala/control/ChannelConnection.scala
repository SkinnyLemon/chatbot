package de.htwg.rs.chatbot
package control

import io.TwitchOutput
import model.TwitchInput

import scala.collection.mutable.ListBuffer

abstract class ChannelConnection extends Subscribable[TwitchInput] {
  def send(message: String, tags: Map[String, String] = Map.empty): Unit
}

class ChannelGateway(output: TwitchOutput, channel: String) extends ChannelConnection with Subscriber[TwitchInput] {
  private val inputHandler = new TwitchInputHandler(this)

  override def onMessage(input: TwitchInput): Unit =
      inputHandler.onMessage(input)
      subscribers.foreach(_.onMessage(input))

  override def send(message: String, tags: Map[String, String] = Map.empty): Unit = output.sendMessage(channel, message, tags)
}

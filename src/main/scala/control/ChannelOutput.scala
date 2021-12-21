package de.htwg.rs.chatbot
package control

import io.TwitchOutput

class ChannelOutput(output: TwitchOutput, channel: String):
  def send(message: String, tags: Map[String, String] = Map.empty): Unit = output.sendMessage(channel, message, tags)

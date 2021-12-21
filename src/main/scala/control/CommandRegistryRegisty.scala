package de.htwg.rs.chatbot
package control

import io.TwitchOutput
import model.{Command, CommandRegistry, TwitchInput}

import scala.collection.mutable

class CommandRegistryRegisty(twitchOutput: TwitchOutput):
  private val channelRegistries = mutable.Map.empty[String, CommandRegistry]

  def addCommand(channelName: String, command: Command): Unit = performOnRegistry(channelName, _.addCommand(command))

  private def performOnRegistry(channelName: String, f: CommandRegistry => CommandRegistry): Unit =
    val registry = getRegistry(channelName)
    channelRegistries(channelName) = f(registry)

  private def getRegistry(channelName: String): CommandRegistry =
    channelRegistries.get(channelName) match
      case Some(registry) => registry
      case None =>
        val channelOutput = new ChannelOutput(twitchOutput, channelName)
        CommandRegistry(channelOutput)

  def removeCommand(channelName: String, command: Command): Unit = performOnRegistry(channelName, _.removeCommand(command))

  def handleMessage(message: TwitchInput): Unit = performOnRegistry(message.channel.name, _.handleMessage(message))

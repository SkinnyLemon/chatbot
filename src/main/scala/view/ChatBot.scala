package de.htwg.rs.chatbot
package view

import control.{CommandRegistryRegisty, TwitchInputProvider, `when message`}
import game.CoinFlipGameHandler
import io.{AkkaAdapter, Config, TwitchConnection}

import akka.stream.scaladsl.{Flow, Sink}
import de.htwg.rs.chatbot.model.TwitchInputParser

object ChatBot {
  def main(args: Array[String]): Unit = {

    val bot = Config.bots.head
    val akkaAdapter = new AkkaAdapter(bot)
    args.foreach(akkaAdapter.connection.join)

    val parser = new TwitchInputParser
    val registry = new CommandRegistryRegisty(akkaAdapter.connection.getOutput)

    val heyCommand = `when message` `starts with` "hey" `respond with` "TheIlluminati"
    val helpCommand = `when message` `contains` "help" `respond with` "Commands: hello, help, play"
    val starWars = `when message` `ends with` "hello there" `respond with` "General Kenobi BrainSlug"
    val helloCommand = `when message` `is` "hello" `respond with` "Hello there! HeyGuys HeyGuys"
    registry.addCommand("imperiabot", heyCommand)
    registry.addCommand("imperiabot", helpCommand)
    registry.addCommand("imperiabot", helloCommand)
    registry.addCommand("imperiabot", new CoinFlipGameHandler())
    registry.addCommand("imperiabot", starWars)

    val processingSink = Flow[String]
      .map(parser.parseToTwitchInput)
      .filter(_.isSuccess)
      .map(_.get)
      .groupBy(Int.MaxValue, _.channel.name)
      .to(Sink.foreach(registry.handleMessage))
    akkaAdapter.start(processingSink)
  }
}

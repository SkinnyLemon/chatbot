package de.htwg.rs.chatbot
package view

import actor.RopePullingGame
import ai.hiLo.HiLoClassifier
import ai.iris.{IrisClassifier, IrisHandler}
import ai.{Classifier, Evaluator}
import control.*
import game.{CoinFlipGameHandler, MlGameHandler}
import io.{AkkaAdapter, Config, TwitchConnection}
import model.{HiLoGame, Iris, TwitchInputParser}

import akka.stream.scaladsl.{Flow, Sink}

import java.io.*
import scala.util.Random

object ChatBot:
  def main(args: Array[String]): Unit =
    val hiLoGameEvaluator: Evaluator[HiLoGame] = new HiLoClassifier().initialize()
    val irisEvaluator: Evaluator[Iris] = new IrisClassifier().initialize()
    val bot = Config.bots.head
    val akkaAdapter = new AkkaAdapter(bot)
    args.foreach(akkaAdapter.connection.join)
    val parser = new TwitchInputParser
    val registry = new CommandRegistryRegisty(akkaAdapter.connection.getOutput)

    val playHelpCommand = `when message` `is` "help -g" `respond with` "2 games available: 'p -c'/'play coinflip': coinflip game; 'p -h'/'play hilo': starts new high low game vs AI"
    val createCommandHelpCommand = `when message` `is` "help -c" `respond with` "create your own command like this:  > !create when message (starts with/is/ends with/contains) \"your commandword\" respond with \"your response\" <"
    val miscHelpCommand = `when message` `is` "help -m" `respond with` "extra commands: 'hey'; 'hello there'; 'hello'"
    val heyCommand = `when message` `starts with` "hey" `respond with` "TheIlluminati"
    val helpCommand = `when message` `contains` "help" `respond with` "Commands: hello, help, play"
    val starWars = `when message` `ends with` "hello there" `respond with` "General Kenobi BrainSlug"
    val helloCommand = `when message` `is` "hello" `respond with` "Hello there! HeyGuys HeyGuys"
    registry.addCommand("imperiabot", heyCommand)
    registry.addCommand("imperiabot", helpCommand)
    registry.addCommand("imperiabot", playHelpCommand)
    registry.addCommand("imperiabot", createCommandHelpCommand)
    registry.addCommand("imperiabot", miscHelpCommand)
    registry.addCommand("imperiabot", helloCommand)
    registry.addCommand("imperiabot", new CoinFlipGameHandler())
    registry.addCommand("imperiabot", new RopePullingGame())
    registry.addCommand("imperiabot", new MlGameHandler(List.empty, hiLoGameEvaluator))
    registry.addCommand("imperiabot", new IrisHandler(irisEvaluator))
    registry.addCommand("imperiabot", starWars)

    val processingSink = Flow[String]
      .map(parser.parseToTwitchInput)
      .filter(_.isSuccess)
      .map(_.get)
      .groupBy(Int.MaxValue, _.channel.name)
      .to(Sink.foreach(registry.handleMessage))
    akkaAdapter.start(processingSink)

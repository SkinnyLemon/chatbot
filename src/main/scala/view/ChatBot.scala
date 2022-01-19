package de.htwg.rs.chatbot
package view

import control.{ChannelOutput, CommandRegistryRegisty, TwitchInputProvider, `when message`}
import game.{CoinFlipGameHandler, MlGameHandler}
import io.{Config, TwitchConnection}
import de.htwg.rs.chatbot.ai.hiLo.HiLoClassifier
import de.htwg.rs.chatbot.control.*
import de.htwg.rs.chatbot.ai.iris.{IrisClassifier, IrisHandler}
import de.htwg.rs.chatbot.ai.{Classifier, Evaluator}
import de.htwg.rs.chatbot.model.{HiLoGame, Iris}
import java.io.{BufferedWriter, File, FileWriter, OutputStreamWriter, PrintWriter}
import scala.util.Random

object ChatBot {
  def main(args: Array[String]): Unit = {

    val hiLoGameEvaluator: Evaluator[HiLoGame] = new HiLoClassifier().initialize()
    val irisEvaluator: Evaluator[Iris] = new IrisClassifier().initialize()

    val bot = Config.bots.head
    val twitchConnection = TwitchConnection.establishConnection(bot.name, bot.auth)
    twitchConnection.start()
    val twitchInput = twitchConnection.getInput
    val twitchOutput = twitchConnection.getOutput

    val registries = new CommandRegistryRegisty(twitchOutput)
    twitchInput.subscribe(new TwitchInputProvider(twitchOutput, twitchConnection, registries))
    args.foreach(twitchConnection.join(_))

    val helpCommand = `when message` `contains` "help" `respond with` "helpflags: -g: games, -c: create commands, -m: misc"
    val playHelpCommand = `when message` `is` "help -g" `respond with` "2 games available: 'p -c'/'play coinflip': coinflip game; 'p -h'/'play hilo': starts new high low game vs AI"
    val createCommandHelpCommand = `when message` `is` "help -c" `respond with` "create your own command like this:  > !create when message (starts with/is/ends with/contains) \"your commandword\" respond with \"your response\" <"
    val miscHelpCommand = `when message` `is` "help -m" `respond with` "extra commands: 'hey'; 'hello there'; 'hello'"
    val heyCommand = `when message` `starts with` "hey" `respond with` "TheIlluminati"
    val starWars = `when message` `ends with` "hello there" `respond with` "General Kenobi BrainSlug"
    val helloCommand = `when message` `is` "hello" `respond with` "Hello there! HeyGuys HeyGuys"

    registries.addCommand("imperiabot", heyCommand)
    registries.addCommand("imperiabot", helpCommand)
    registries.addCommand("imperiabot", playHelpCommand)
    registries.addCommand("imperiabot", createCommandHelpCommand)
    registries.addCommand("imperiabot", miscHelpCommand)
    registries.addCommand("imperiabot", helloCommand)
    registries.addCommand("imperiabot", new CoinFlipGameHandler())
    registries.addCommand("imperiabot", new MlGameHandler(List.empty, hiLoGameEvaluator))
    registries.addCommand("imperiabot", new IrisHandler(irisEvaluator))
    registries.addCommand("imperiabot", starWars)
  }

}


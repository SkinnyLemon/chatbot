package de.htwg.rs.chatbot
package view

import control.{ChannelOutput, CommandRegistryRegisty, TwitchInputProvider, `when message`}
import game.{CoinFlipGameHandler, MlGameHandler}
import io.{Config, TwitchConnection}

import de.htwg.rs.chatbot.ai.HiLoClassifier
import de.htwg.rs.chatbot.control.*
import de.htwg.rs.chatbot.iris.{Evaluator, IrisClassifier, IrisHandler}
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


    val heyCommand = `when message` `starts with` "hey" `respond with` "TheIlluminati"
    val helpCommand = `when message` `contains` "help" `respond with` "Commands: hello, help, play"
    val starWars = `when message` `ends with` "hello there" `respond with` "General Kenobi BrainSlug"
    val helloCommand = `when message` `is` "hello" `respond with` "Hello there! HeyGuys HeyGuys"


    registries.addCommand("imperiabot", heyCommand)
    registries.addCommand("imperiabot", helpCommand)
    registries.addCommand("imperiabot", helloCommand)
    registries.addCommand("imperiabot", new CoinFlipGameHandler())
    registries.addCommand("imperiabot", new MlGameHandler(List.empty, hiLoGameEvaluator))
    registries.addCommand("imperiabot", new IrisHandler(irisEvaluator))
    registries.addCommand("imperiabot", starWars)


    //asdf()
    //    getOptimalSolution(10, List.empty) //t
    //
    //    getOptimalSolution(7 , List(10)) //h
    //    getOptimalSolution(10 , List(12)) //t
    //
    //    getOptimalSolution(10 , List(12,11)) //t
    //    getOptimalSolution(8 , List(9,12)) //h
    //    getOptimalSolution(10 , List(11,8, 7,9)) //l

    //    (1 to 1000).foreach(_ => {
    //      asdf()
    //    })


  }

  def asdf() = {

    val cardDeck = List(7, 8, 9, 10, 11, 12)
    val shuffledDeck = Random.shuffle(cardDeck)

    val firstNumber = shuffledDeck(0)
    val s1 = getOptimalSolution(firstNumber, List.empty)
    writeToFile(List(firstNumber, 0, 0, 0, 0), s1)

    val second = shuffledDeck(1)
    val s2 = getOptimalSolution(second, List(firstNumber))
    writeToFile(List(firstNumber, second, 0, 0, 0), s2)

    val third = shuffledDeck(2)
    val s3 = getOptimalSolution(third, List(firstNumber, second))
    writeToFile(List(firstNumber, second, third, 0, 0), s3)

    val fourth = shuffledDeck(3)
    val s4 = getOptimalSolution(fourth, List(firstNumber, second, third))
    writeToFile(List(firstNumber, second, third, fourth, 0), s4)

    val fifth = shuffledDeck(4)
    val s5 = getOptimalSolution(fifth, List(firstNumber, second, third, fourth))
    writeToFile(List(firstNumber, second, third, fourth, fifth), s5)


  }

  // 7 (8) 9 (10) (11) 12

  def getOptimalSolution(lastDrawn: Int, numbers: List[Int]): Int = {

    var numberOfHigherCards = 0
    var numberOfLowerCards = 0

    ((lastDrawn) to 12).foreach(number => {
      if (!numbers.contains(number)) {
        numberOfHigherCards += 1
      }
    })
    numberOfHigherCards -= 1

    (7 until lastDrawn).foreach(number => {
      if (!numbers.contains(number)) {
        numberOfLowerCards += 1
      }
    })

    println(numberOfHigherCards + " vs " + numberOfLowerCards)

    if (numberOfHigherCards == numberOfLowerCards) {
      if (Random.nextBoolean())
        1
      else
        0
    }

    if (numberOfHigherCards > numberOfLowerCards) {
      //println("higher")
      1
    } else {
      //println("lower")
      0
    }


  }


  def writeToFile(numbers: List[Int], optimalSolution: Int): Unit = {
    val bw = new BufferedWriter(new FileWriter(new File("file.out.txt"), true))

    numbers.foreach(n => {
      bw.write(s"$n,")
    })
    bw.write(s"$optimalSolution\n")
    bw.close
  }

}


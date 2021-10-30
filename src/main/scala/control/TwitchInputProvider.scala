package de.htwg.rs.chatbot
package control

import io.{TwitchConnection, TwitchConsumer, TwitchOutput}
import model.{TwitchInput, TwitchInputParser}

import de.htwg.rs.chatbot.game.*

import scala.util.{Failure, Success, Try}

class TwitchInputProvider(output: TwitchOutput, twitchConnection: TwitchConnection) extends TwitchConsumer {
  val parser = new TwitchInputParser()
  val commandPrefix = '!'

  override def onMessage(message: String): Unit =

    if (message.contains("PRIVMSG"))
      val twitchInput = parser.parseToTwitchInput(message) match {
        case Success(twitchInput) =>
          println(twitchInput)
          if (isCommand(twitchInput.message.text))
            executeCommand(twitchInput, twitchInput.message.text)
        case Failure(error) =>
          error.printStackTrace()
      }


  def executeCommand(input: TwitchInput, params: String): Unit =
    val parentReplyMap = Map("reply-parent-msg-id" -> input.message.id)
    val parameterArray = params.toLowerCase.replace("--", "-").split("-")
    val flags = parameterArray.drop(1)
    val parameter = parameterArray(0).replace(" ", "").drop(1)
    parameter match {
      case "hello" => output.sendMessage("imperiabot", s"Hello there, ${input.user.displayName}! HeyGuys HeyGuys ", parentReplyMap)
      case "help" => output.sendMessage("imperiabot", s"${commandPrefix}hello: Says Hello; ${commandPrefix}help: Displays commands; ${commandPrefix}p/play -c/coinflip: starts a new coinflip game", parentReplyMap)
      case "p" => startGame(flags, input, parentReplyMap)
      case "play" => startGame(flags, input, parentReplyMap)
      case _ => output.sendMessage("imperiabot", s"'${commandPrefix}${parameter}' is not a valid command. Type '${commandPrefix}help' to get help", parentReplyMap)
    }

  def startGame(flags: Array[String], input: TwitchInput, parentReplyMap: Map[String, String]) =

    println("flags")
    flags.foreach(print)
    print(flags(0))
    println("/flags")


    flags(0) match {
      case "c" => new CoinFlipGame(input, output, twitchConnection, "imperiabot")
      case "coinflip" => new CoinFlipGame(input, output, twitchConnection, "imperiabot")
      case _ => output.sendMessage("imperiabot", s"'${flags(0)}' is no valid option. Type '${commandPrefix}help' to get help", parentReplyMap)
    }

  def isCommand(message: String): Boolean =
    message.startsWith(commandPrefix.toString)


}


package de.htwg.rs.chatbot
package control

import game.CoinFlipGame
import model.TwitchInput

import de.htwg.rs.chatbot.io.TwitchOutput

class ChannelInputHandler(output: ChannelOutput) extends Subscriber[TwitchInput] {
  val commandPrefix = '!'

  override def onMessage(input: TwitchInput): Unit =
    val params = input.message.text
    if (isCommand(params))
      val parentReplyMap = Map("reply-parent-msg-id" -> input.message.id)
      val parameterArray = params.toLowerCase.replace("--", "-").split("-")
      val flags = parameterArray.drop(1)
      val parameter = parameterArray(0).replace(" ", "").drop(1)
      parameter match {
        case "hello" => output.send(s"Hello there, ${input.user.displayName}! HeyGuys HeyGuys ", parentReplyMap)
        case "help" => output.send(s"${commandPrefix}hello: Says Hello; ${commandPrefix}help: Displays commands; ${commandPrefix}p/play -c/coinflip: starts a new coinflip game", parentReplyMap)
        case "p" => startGame(flags, input, parentReplyMap)
        case "play" => startGame(flags, input, parentReplyMap)
        case _ => output.send(s"'${commandPrefix}${parameter}' is not a valid command. Type '${commandPrefix}help' to get help", parentReplyMap)
      }

  def startGame(flags: Array[String], input: TwitchInput, parentReplyMap: Map[String, String]) =
    println("flags")
    flags.foreach(print)
    print(flags(0))
    println("/flags")

  //    flags(0) match {
  //      case "c" => new CoinFlipGame(input, output, twitchConnection, "imperiabot")
  //      case "coinflip" => new CoinFlipGame(input, output, twitchConnection, "imperiabot")
  //      case _ => output.send(s"'${flags(0)}' is no valid option. Type '${commandPrefix}help' to get help", parentReplyMap)
  //    }

  def isCommand(message: String): Boolean =
    message.startsWith(commandPrefix.toString)
}

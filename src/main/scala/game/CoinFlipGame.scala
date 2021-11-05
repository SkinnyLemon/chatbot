package de.htwg.rs.chatbot
package game

import scala.util.Random
import io.{TwitchConnection, TwitchConsumer, TwitchOutput}

import de.htwg.rs.chatbot.model.{TwitchInput, TwitchInputParser}

import scala.collection.MapOps
import scala.util.{Failure, Success}

class CoinFlipGame(parentInput: TwitchInput, output: TwitchOutput, twitchConnection: TwitchConnection, channel: String, playerWins: Int = 0, playerLosses: Int = 0, isNewInstance: Boolean = true) extends TwitchConsumer {
  val parentUserId = parentInput.user.userId
  val parser = new TwitchInputParser()
  val playerName = parentInput.user.displayName
  val parentMessageMap = Map("reply-parent-msg-id" -> parentInput.message.id)
  //val playerWins = 0 //TODO is there a way to track the score without var?
  //var playerLosses = 0

  twitchConnection.getInput.subscribe(this)

  if (isNewInstance) {
    output.sendMessage(channel, s"started a new flip a coin game for player ${playerName}! Make your choice: (h)ead or (t)ails. (s)core to see your results. (e)xit to end the game.", parentMessageMap)
  }


  override def onMessage(message: String): Unit =
    if (message.contains("PRIVMSG"))
      val twitchInput = parser.parseToTwitchInput(message) match
        case Success(twitchInput) =>
          if (twitchInput.user.userId == parentUserId)
            processInput(twitchInput)
        case Failure(error) => error.printStackTrace()


  def processInput(input: TwitchInput): Unit =
    input.message.text.toLowerCase() match
      case "h" => processGame("head")
      case "head" => processGame("head")
      case "t" => processGame("tails")
      case "tails" => processGame("tails")
      case "s" => sendResult()
      case "score" => sendResult()
      case "e" => endGame()
      case "exit" => endGame()
      case _ => output.sendMessage(channel, "illegal move. Use (h)ead or (t)ails!", parentMessageMap)


  def processGame(choice: String) =
    output.sendMessage(channel, s"Your pick: ${choice}.", parentMessageMap)
    output.sendMessage(channel, "Flipping a coin! Good luck!", parentMessageMap)
    val coinFlipResult = coinFlip()
    output.sendMessage(channel, coinFlipResult, parentMessageMap)
    val matchResultMessage = generateMatchResultMessage(coinFlipResult.toLowerCase == choice)
    output.sendMessage(channel, matchResultMessage, parentMessageMap)
    output.sendMessage(channel, "Play again? Just continue. To stop playing type (e)xit", parentMessageMap)

  def generateMatchResultMessage(isPlayerWin: Boolean): String =
    if (isPlayerWin)
      //playerWins = playerWins + 1
      updateCoinflipGameInstance(playerWins + 1, playerLosses)
      "WutFace WutFace H0w did that happen?! you won! NotLikeThis  Anyway. Congrats Kappa Kappa "
    else
      //playerLosses = playerLosses + 1
      updateCoinflipGameInstance(playerWins, playerLosses + 1)
      "O neim! du hast verloren!!! lelelelel PogChamp PogChamp PogChamp"

  def sendResult(): Unit =
    output.sendMessage(channel, s"You won ${playerWins} games. You lost ${playerLosses} games.", parentMessageMap)

  def coinFlip(): String = if (Random.nextBoolean()) "Head" else "Tails"

  def endGame() =
    output.sendMessage(channel, "ok baiii", parentMessageMap)
    twitchConnection.getInput.unSubscribe(this)

  def updateCoinflipGameInstance(playerWins: Int, playerLosses: Int) =
    twitchConnection.getInput.unSubscribe(this)
    new CoinFlipGame(parentInput, output, twitchConnection, channel, playerWins, playerLosses, false)
}

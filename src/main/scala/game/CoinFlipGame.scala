package de.htwg.rs.chatbot
package game

import model.{TwitchInput, User}

import scala.util.Random

case class CoinFlipGame(player: User, wins: Int = 0, losses: Int = 0) {
  def handle(input: TwitchInput): (Option[CoinFlipGame], Option[String]) =
    if (input.user.userId == player.userId)
      processInput(input)
    else
      (Some(this), None)

  private def processInput(input: TwitchInput): (Option[CoinFlipGame], Option[String]) =
    input.message.text.toLowerCase() match {
      case "h" | "head" => processGame(input, "head")
      case "t" | "tails" => processGame(input, "tails")
      case "s" | "score" => sendResult(input)
      case "e" | "exit" => endGame(input)
      case _ => (Some(this), None)
    }

  private def processGame(input: TwitchInput, choice: String): (Option[CoinFlipGame], Option[String]) = {
    val coinFlipResult = coinFlip()
    val isWin = coinFlipResult.toLowerCase == choice
    val matchResultMessage = generateMatchResultMessage(isWin)
    (Some(nextRound(isWin)), Some(matchResultMessage))
  }

  private def generateMatchResultMessage(isWin: Boolean): String =
    if (isWin)
      "WutFace WutFace H0w did that happen?! you won! NotLikeThis  Anyway. Congrats Kappa Kappa "
    else
      "O neim! du hast verloren!!! lelelelel PogChamp PogChamp PogChamp"

  private def nextRound(isWin: Boolean): CoinFlipGame =
    if (isWin)
      copy(wins = wins + 1)
    else
      copy(losses = losses + 1)

  private def sendResult(input: TwitchInput): (Option[CoinFlipGame], Option[String]) =
    (Some(this), Some(s"You won ${wins} games. You lost ${losses} games."))

  private def coinFlip(): String = if (Random.nextBoolean()) "Head" else "Tails"

  private def endGame(input: TwitchInput): (Option[CoinFlipGame], Option[String]) =
    (None, Some("ok baiii"))
}

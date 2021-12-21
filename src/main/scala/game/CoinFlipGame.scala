package de.htwg.rs.chatbot
package game

import model.{TwitchInput, User}

import scala.util.Random

case class CoinFlipGame(player: User, wins: Int = 0, losses: Int = 0):
  def handle(input: TwitchInput): (Option[CoinFlipGame], Option[String]) =
    if (input.user.userId == player.userId)
      processInput(input)
    else
      (Some(this), None)

  private def processInput(input: TwitchInput): (Option[CoinFlipGame], Option[String]) =
    input.message.text.toLowerCase() match
      case "h" | "head" => processGame("head")
      case "t" | "tails" => processGame("tails")
      case "s" | "score" => sendResult()
      case "e" | "exit" => endGame()
      case _ => (Some(this), None)

  private def processGame(choice: String): (Option[CoinFlipGame], Option[String]) =
    val coinFlipResult = coinFlip()
    val isWin = coinFlipResult.toLowerCase == choice
    val matchResultMessage = generateMatchResultMessage(isWin)
    (Some(nextRound(isWin)), Some(matchResultMessage))

  private def generateMatchResultMessage(isWin: Boolean): String =
    if (isWin)
      "WutFace How did that happen?! You won and I lost! NotLikeThis  Congrats! SeemsGood SeemsGood"
    else
      "Oh no! You lost! NotLikeThis better luck next time! SeemsGood"

  private def nextRound(isWin: Boolean): CoinFlipGame =
    if (isWin)
      copy(wins = wins + 1)
    else
      copy(losses = losses + 1)

  private def coinFlip(): String = if (Random.nextBoolean()) "Head" else "Tails"

  private def sendResult(): (Option[CoinFlipGame], Option[String]) =
    (Some(this), Some(s"You won ${wins} games. You lost ${losses} games."))

  private def endGame(): (Option[CoinFlipGame], Option[String]) =
    (None, Some("Ok. See you arround!"))

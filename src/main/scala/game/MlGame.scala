package de.htwg.rs.chatbot
package game

import model.{HiLoGame, TwitchInput, User}

import de.htwg.rs.chatbot.ai.{Classifier, Evaluator}

case class MlGame(player: User, game: Running, hiLoGameEvaluator: Evaluator[HiLoGame]) {
  def handle(input: TwitchInput): (Option[MlGame], Option[String]) =
    if (input.user.userId == player.userId)
      processInput(input)
    else
      (Some(this), None)

  private def processInput(input: TwitchInput): (Option[MlGame], Option[String]) =
    input.message.text.toLowerCase() match
      case "m" | "h" => processResult(game.betMore)
      case "l" | "t" => processResult(game.betLess)
      case "s" | "score" => (Some(this), Some(s"Streak: ${game.streak}"))
      case "e" | "exit" => (None, Some("Ok. See you arround!"))
      case _ => (Some(this), None)

  private def processResult(playerResult: MLCore): (Option[MlGame], Option[String]) =
    playerResult match {
      case w: Win => (None, Some(generatePlayerWinsGameOverMessage(w.current, w.streak)))
      case l: Loss => (None, Some(generatePlayerWrongGuessMessage(l.current, l.streak)))
      case player: Running => generateAiDecision(player) match {
        case l: Loss => (None, Some(generateAiGuessedWrongMessage(player.current, l.current, l.streak)))
        case w: Win => (None, Some(generateAiWinsGameOverMessage(player.current, w.current, w.streak)))
        case ai: Running => (Some(copy(game = ai)), Some(generateRunningMessage(player.current, ai.current, ai.streak)))
      }
    }


  private def generateAiDecision(game: Running): MLCore = {
    val first = if(game.discardPile.length >= 1) then game.discardPile(0) else 0
    val second = if(game.discardPile.length >= 2) then game.discardPile(1) else 0
    val third = if(game.discardPile.length >= 3) then game.discardPile(2) else 0
    val fourth = if(game.discardPile.length >= 4) then game.discardPile(3) else 0
    val fifth = if(game.discardPile.length >= 5) then game.discardPile(4) else 0

    val gameState = HiLoGame(first, second, third, fourth, fifth)
    val gameStateWithPrediction = hiLoGameEvaluator.evaluate(gameState)
    if (gameStateWithPrediction.bestChoice == 1) then
      game.betMore
    else
      game.betLess
  }

  private def generatePlayerWinsGameOverMessage(playerCard: Int, currentStreak: Int): String =
    s"Correct! Game Over, you win. Card was a ${playerCard}. You got a streak of ${currentStreak}"


  private def generatePlayerWrongGuessMessage(lastCard: Int, currentStreak: Int): String =
      s"Too bad, you loose! The card drawn was a ${lastCard}. You got a streak of ${currentStreak}."


  private def generateAiGuessedWrongMessage(playerCard: Int, aiCard: Int, currentStreak: Int): String =
    s"Correct! next card was a ${playerCard}. Ai guessed wrong and has drawn a: ${aiCard} => Game over. Player wins! Streak: ${currentStreak}"

  private def generateAiWinsGameOverMessage(playerCard: Int, aiCard: Int, currentStreak: Int): String =
    s"Correct! next card was a ${playerCard}. Ai guessed correct and has drawn a: ${aiCard} => Game over. Both win! Streak: ${currentStreak}"

  private def generateRunningMessage(playerCard: Int, aiCard: Int, currentStreak: Int): String =
    s"Correct! next card was a ${playerCard}. Ai had a correct guess for the card drawn: ${aiCard}. The game is still going. Current Streak: ${currentStreak}. Will you go (h)igher or (l)ower than ${aiCard}?"


}

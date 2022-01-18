package de.htwg.rs.chatbot
package game

import model.{TwitchInput, User}

case class MlGame(player: User, game: Running) {
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
      case "e" | "exit" => (None, Some("ok baiii"))
      case _ => (Some(this), None)

  private def processResult(playerResult: MLCore): (Option[MlGame], Option[String]) =
    playerResult match {
      case w: Win => (None, Some(generatePlayerWinsGameOverMessage(w.current, w.streak)))
      case l: Loss => (None, Some(generatePlayerWrongGuessMessage(l.current, l.streak)))
      case player: Running => MlAi.play(player) match {
        case l: Loss => (None, Some(generateAiGuessedWrongMessage(player.current, l.current, l.streak)))
        case w: Win => (None, Some(generateAiWinsGameOverMessage(player.current, w.current, w.streak)))
        case ai: Running => (Some(copy(game = ai)), Some(generateRunningMessage(player.current, ai.current, ai.streak)))
      }
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

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
      case w: Win => (None, Some(s"${w.current} => Player wins: ${w.streak} streak"))
      case l: Loss => (None, Some(s"${l.current} => Player loses: ${l.streak} streak"))
      case player: Running => MlAi.play(player) match {
        case l: Loss => (None, Some(s"${player.current}, ${l.current} => AI loses: ${l.streak} streak"))
        case w: Win => (None, Some(s"${player.current}, ${w.current} => AI wins: ${w.streak} streak"))
        case ai: Running => (Some(copy(game = ai)), Some(s"${player.current}, ${ai.current} => Still going: ${ai.streak} streak"))
      }
    }
}

package de.htwg.rs.chatbot
package game

import model.{Command, HiLoGame, TwitchInput}

import de.htwg.rs.chatbot.ai.{Classifier, Evaluator}

case class MlGameHandler(instances: List[MlGame] = List.empty, hiLoGameEvaluator: Evaluator[HiLoGame]) extends Command {

  override def handle(input: TwitchInput): (Command, Option[String]) = input.message.text match {
    case "p -h" | "play hilo" => startGame(input)
    case _ =>
      var response: Option[String] = None
      val newInstances = instances.map(_.handle(input))
        .map { case (game, gameResponse) =>
          gameResponse.foreach(r => response = Some(r))
          game
        }
        .filter(_.isDefined)
        .map(_.get)
      (copy(newInstances, hiLoGameEvaluator), response)
  }

  def startGame(input: TwitchInput): (Command, Option[String]) =
    val game = MLCore.startGame(7, 12)
    val newGame = MlGame(input.user, game, hiLoGameEvaluator)
    (copy(instances = instances :+ newGame, hiLoGameEvaluator), Some(s"started a new higher-lower game for player ${input.user.displayName}! First card: ${game.current}!"))
}

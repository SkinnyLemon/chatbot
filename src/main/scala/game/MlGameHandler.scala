package de.htwg.rs.chatbot
package game

import model.{Command, TwitchInput}

case class MlGameHandler(instances: List[MlGame] = List.empty) extends Command {

  override def handle(input: TwitchInput): (Command, Option[String]) = input.message.text match {
    case "p2" | "play2" => startGame(input)
    case _ =>
      var response: Option[String] = None
      val newInstances = instances.map(_.handle(input))
        .map { case (game, gameResponse) =>
          gameResponse.foreach(r => response = Some(r))
          game
        }
        .filter(_.isDefined)
        .map(_.get)
      (copy(newInstances), response)
  }

  def startGame(input: TwitchInput): (Command, Option[String]) =
    val game = MLCore.startGame(7, 12)
    val newGame = MlGame(input.user, game)
    (copy(instances = instances :+ newGame), Some(s"started a new higher-lower game for player ${input.user.displayName}! First card: ${game.current}!"))
}

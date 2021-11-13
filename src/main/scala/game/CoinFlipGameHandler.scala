package de.htwg.rs.chatbot
package game

import model.{Command, TwitchInput}

import de.htwg.rs.chatbot.control.ChannelOutput

case class CoinFlipGameHandler(instances: List[CoinFlipGame] = List.empty) extends Command {
  override def handle(input: TwitchInput): (Command, Option[String]) = input.message.text match {
    case "p" | "play" => startGame(input)
    case _ =>
      var response: Option[String] = None
      val newInstances = instances.map(_.handle(input))
          .map{ case (game, gameResponse) => {
            gameResponse.foreach(r => response = Some(r))
            game
          }}
          .filter(_.isDefined)
          .map(_.get)
      (copy(newInstances), response)
  }

  def startGame(input: TwitchInput): (Command, Option[String]) =
    val newGame = new CoinFlipGame(input.user)
    (copy(instances = instances :+ newGame), Some(s"started a new flip a coin game for player ${input.user.displayName}! Make your choice: (h)ead or (t)ails. (s)core to see your results. (e)xit to end the game."))
}

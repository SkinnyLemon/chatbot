package de.htwg.rs.chatbot
package game

import scala.util.Random

object MlAi {
  def play(game: Running): MLCore =
    if (Random.nextBoolean())
      game.betLess
    else
      game.betMore
}

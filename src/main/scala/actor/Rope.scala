package de.htwg.rs.chatbot
package actor

import akka.actor.Actor
import Messages._

class Rope extends Actor {

  var distanceFromMiddle = 0

  def receive = {
    case PullLeft => distanceFromMiddle -= 1
    case PullRight => distanceFromMiddle += 1
    case GetScore => sender() ! ("score is " + distanceFromMiddle)
    case GetWinner => "Game Over. Distance is " + distanceFromMiddle + ". " + getWinnerString(distanceFromMiddle)

  }

  def getWinnerString(distanceFromMiddle: Int): String = {
    if (distanceFromMiddle == 0) return "Its a draw! Everyone wins!"
    if (distanceFromMiddle < 0) "Team Left wins!" else "Team Right wins!"
  }


}

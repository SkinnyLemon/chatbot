package de.htwg.rs.chatbot
package anotherActor

import akka.actor.Actor
import Messages._

class Rope extends Actor {

  var distanceFromMiddle = 0

  def receive = {
    case PullLeft => distanceFromMiddle -= 1
    case PullRight => distanceFromMiddle += 1
    case PrintScore => println("score is" + distanceFromMiddle)
    case AnnounceWinner => println("Game Over. Distance is" + distanceFromMiddle + ". Therefore team " + getWinnerString(distanceFromMiddle))

  }


  def getWinnerString(distanceFromMiddle: Int): String = {
    if (distanceFromMiddle == 0) return "Its a draw! Everyone wins!"
    if (distanceFromMiddle < 0) "Left wins!" else "Right wins!"
  }


}

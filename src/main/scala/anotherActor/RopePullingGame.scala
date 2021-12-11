package de.htwg.rs.chatbot
package anotherActor

import akka.actor.{ActorRef, ActorSystem, Props}
import Messages._
import de.htwg.rs.chatbot.model.{Command, TwitchInput}


case class RopePullingGame(ropes: List[ActorRef] = List.empty) extends Command {

  val system: ActorSystem = ActorSystem("SimpleSystem");
  var ropeInstance = 0

  override def handle(input: TwitchInput): (Command, Option[String]) = input.message.text match {

    case "new rope" => newGame(input)
    case _ => (copy(ropes), None)
    //case "l" | "left" => pullleft(input)
    //    case _ =>
    //      var response: Option[String] = None
    //      val newInstances = instances.map(_.handle(input))
    //        .map { case (game, gameResponse) => {
    //          gameResponse.foreach(r => response = Some(r))
    //          game
    //        }
    //        }
    //        .filter(_.isDefined)
    //        .map(_.get)
    //      (copy(newInstances), response)
  }

  def newGame(input: TwitchInput): (RopePullingGame, Option[String]) = {
    val newRope = system.actorOf(Props[Rope], s"Rope-$ropeInstance")
    (copy(ropes = ropes :+ newRope), Some(s"created a new Rope for ${input.user.displayName}!"))
  }


  //val system = ActorSystem("SimpleSystem")
  val rope1 = system.actorOf(Props[Rope], "rope1")
  val rope2 = system.actorOf(Props[Rope], "rope2")
  val rope3 = system.actorOf(Props[Rope], "rope3")

  rope1 ! PullLeft
  rope1 ! PullLeft
  rope2 ! PullLeft
  rope3 ! PullLeft


  rope1 ! PullRight
  rope1 ! PullRight
  rope2 ! PullRight
  rope2 ! PullRight

  rope1 ! AnnounceWinner
  rope2 ! AnnounceWinner
  rope3 ! AnnounceWinner


}

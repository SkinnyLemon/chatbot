package de.htwg.rs.chatbot
package actor

import akka.actor.{ActorRef, ActorSystem, Props}
import Messages._
import akka.pattern.ask
import akka.util.Timeout
import de.htwg.rs.chatbot.model.{Command, TwitchInput}

import scala.concurrent.Await
import scala.concurrent.duration.DurationInt
import scala.language.postfixOps


case class RopePullingGame(ropes: List[ActorRef] = List.empty) extends Command {

  val system: ActorSystem = ActorSystem("RopeSystem");
  var ropeInstance = 0

  override def handle(input: TwitchInput): (Command, Option[String]) = input.message.text match {
    case "new rope" => newGame(input)
    case "let loose" | "end game" | "alt f4" => endGame(input.user.displayName)
    case "print score" => printScore(input.user.displayName)
    case text if text startsWith ("pull") => executePullOnPlayer(text) //TODO parse player, execute
    case _ => (copy(ropes), None)
  }

  def newGame(input: TwitchInput): (RopePullingGame, Option[String]) = {
    val newRope = system.actorOf(Props[Rope], s"Rope-${
      input.user.displayName
    }")
    (copy(ropes = ropes :+ newRope), Some(s"created a new Rope for ${
      input.user.displayName
    }!"))
  }

  def executePullOnPlayer(text: String): (Command, Option[String]) = {
    val nameOption = "@.*".r findFirstIn text
    val direction = text.split(" ")(1)

    val name = nameOption match {
      case Some(name) => name substring (1) //strips '@' from name
      case None => return (copy(ropes), None) //TODO better way than to return here?
    }

    val directionMessage = direction match {
      case "left" => PullLeft
      case "right" => PullRight
      case _ => return (copy(ropes), None) //TODO better way than to return here?
    }

    val ropeInstance = ropes.find(_.toString().contains(name))
    val ropeActor = ropeInstance match {
      case Some(instance) => instance
      case None => return (copy(ropes), None)
    }

    ropeActor ! directionMessage

    (copy(ropes), None)
  }


  def endGame(displayName: String): (Command, Option[String]) = {
    println("ending game for username: " + displayName)
    val ropeInstance = ropes.find(_.toString().contains(displayName))
    val ropeActor = ropeInstance match {
      case Some(instance) => instance
      case None => return (copy(ropes), None)
    }

    implicit val timeout = Timeout(1 seconds)
    val future = ropeActor ? GetScore
    val result = Await.result(future, timeout.duration).asInstanceOf[String]

    val updatedRopeList = ropes.filter(_ != ropeActor)

    system.stop(ropeActor)


    (copy(updatedRopeList), Some(result))
  }

  def printScore(displayName: String): (Command, Option[String]) = {
    val ropeInstance = ropes.find(_.toString().contains(displayName))
    val ropeActor = ropeInstance match {
      case Some(instance) => instance
      case None => return (copy(ropes), None)
    }

    implicit val timeout = Timeout(1 seconds)
    val future = ropeActor ? GetScore
    val result = Await.result(future, timeout.duration).asInstanceOf[String]

    (copy(ropes), Some(result))
  }


}

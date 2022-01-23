package de.htwg.rs.chatbot
package actor

import akka.actor.{ActorRef, ActorSystem, Props}
import de.htwg.rs.chatbot.model.{Message, TwitchInput, TwitchInputParser, User}
import org.mockito.Mockito.{times, verify, when}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.mockito.MockitoSugar.mock


class RopePullingGameTest extends AnyWordSpec with Matchers {

  val twitchInputMock = mock[TwitchInput]
  when(twitchInputMock.user).thenReturn(User("Player1", "Player1", "id-1234"))

  "A RopePullingGame input parser" should {

    "handle input for new rope" in {
      val ropePullingGame = RopePullingGame();
      val messageObject = Message(Array.empty, "new rope", 123123, "123123")
      when(twitchInputMock.message).thenReturn(messageObject)
      val newRope = ropePullingGame.handle(twitchInputMock)
      newRope._1 shouldBe a[RopePullingGame]
      newRope._2.get shouldBe "created a new Rope for Player1!"
    }

    "not do anything when input is invalid command" in {
      val ropePullingGame = RopePullingGame();
      val messageObject = Message(Array.empty, "non-valid-command", 123123, "123123")
      when(twitchInputMock.message).thenReturn(messageObject)
      val newRope = ropePullingGame.handle(twitchInputMock)
      newRope._1 shouldBe ropePullingGame
      newRope._2 shouldBe None
    }

    "end game on let loose command" in {
      val ropePullingGame = RopePullingGame();
      val messageObject = Message(Array.empty, "let loose", 123123, "123123")
      when(twitchInputMock.message).thenReturn(messageObject)
      val endGame = ropePullingGame.handle(twitchInputMock)
      endGame._1 shouldBe RopePullingGame(List())
      endGame._2 shouldBe None
    }

    "end game on let loose command with present actor in list" in {
      val system: ActorSystem = ActorSystem("RopeSystem");
      val newRope = system.actorOf(Props[Rope](), s"Rope-Player1")
      val ropePullingGame = RopePullingGame(List(newRope));
      val messageObject = Message(Array.empty, "let loose", 123123, "123123")
      when(twitchInputMock.message).thenReturn(messageObject)
      val endGame = ropePullingGame.handle(twitchInputMock)
      endGame._1 shouldBe RopePullingGame(List())
      endGame._2.get shouldBe "Game Over. Distance is 0. Its a draw! Everyone wins!"
    }

    "print the score on Print score command" in {
      val system: ActorSystem = ActorSystem("RopeSystem");
      val newRope = system.actorOf(Props[Rope](), s"Rope-Player1")
      val ropePullingGame = RopePullingGame(List(newRope));
      val messageObject = Message(Array.empty, "print score", 123123, "123123")
      when(twitchInputMock.message).thenReturn(messageObject)
      val endGame = ropePullingGame.handle(twitchInputMock)
      endGame._1 shouldBe RopePullingGame(List(newRope))
      endGame._2.get shouldBe "score is 0"
    }

    "not print the score on Print score command when no active game" in {
      val ropePullingGame = RopePullingGame();
      val messageObject = Message(Array.empty, "print score", 123123, "123123")
      when(twitchInputMock.message).thenReturn(messageObject)
      val endGame = ropePullingGame.handle(twitchInputMock)
      endGame._1 shouldBe RopePullingGame(List())
      endGame._2 shouldBe None
    }

    "have the correct score after pulling left" in {
      val system: ActorSystem = ActorSystem("RopeSystem");
      val newRope = system.actorOf(Props[Rope](), s"Rope-Player1")
      val ropePullingGame = RopePullingGame(List(newRope));
      val messageObject = Message(Array.empty, "pull left @Player1", 123123, "123123")
      when(twitchInputMock.message).thenReturn(messageObject)

      val runningGame = ropePullingGame.handle(twitchInputMock)
      runningGame._1 shouldBe ropePullingGame
      runningGame._2 shouldBe None

      when(twitchInputMock.message).thenReturn (Message (Array.empty, "print score", 123123, "123123"))
      val endGame = ropePullingGame.handle(twitchInputMock)
      endGame._2.get shouldBe "score is -1"
    }

    "have the correct score after pulling right" in {
      val system: ActorSystem = ActorSystem("RopeSystem");
      val newRope = system.actorOf(Props[Rope](), s"Rope-Player1")
      val ropePullingGame = RopePullingGame(List(newRope));
      val messageObject = Message(Array.empty, "pull right @Player1", 123123, "123123")
      when(twitchInputMock.message).thenReturn(messageObject)

      val runningGame = ropePullingGame.handle(twitchInputMock)
      runningGame._1 shouldBe ropePullingGame
      runningGame._2 shouldBe None

      when(twitchInputMock.message).thenReturn (Message (Array.empty, "print score", 123123, "123123"))
      val endGame = ropePullingGame.handle(twitchInputMock)
      endGame._2.get shouldBe "score is 1"
    }

    "not be pulling when player name is wrong" in {
      val system: ActorSystem = ActorSystem("RopeSystem");
      val newRope = system.actorOf(Props[Rope](), s"Rope-Player1")
      val ropePullingGame = RopePullingGame(List(newRope));
      val messageObject = Message(Array.empty, "pull right @Player3", 123123, "123123")
      when(twitchInputMock.message).thenReturn(messageObject)

      val runningGame = ropePullingGame.handle(twitchInputMock)
      runningGame._1 shouldBe ropePullingGame
      runningGame._2 shouldBe None

      when(twitchInputMock.message).thenReturn (Message (Array.empty, "print score", 123123, "123123"))
      val endGame = ropePullingGame.handle(twitchInputMock)
      endGame._2.get shouldBe "score is 0"
    }

    "not be pulling when direction is wrong" in {
      val system: ActorSystem = ActorSystem("RopeSystem");
      val newRope = system.actorOf(Props[Rope](), s"Rope-Player1")
      val ropePullingGame = RopePullingGame(List(newRope));
      val messageObject = Message(Array.empty, "pull up @Player1", 123123, "123123")
      when(twitchInputMock.message).thenReturn(messageObject)

      val runningGame = ropePullingGame.handle(twitchInputMock)
      runningGame._1 shouldBe ropePullingGame
      runningGame._2 shouldBe None

      when(twitchInputMock.message).thenReturn (Message (Array.empty, "print score", 123123, "123123"))
      val endGame = ropePullingGame.handle(twitchInputMock)
      endGame._2.get shouldBe "score is 0"
    }

  }

}

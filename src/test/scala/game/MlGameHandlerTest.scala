package de.htwg.rs.chatbot
package game

import de.htwg.rs.chatbot.ai.Evaluator
import de.htwg.rs.chatbot.model.{Command, Message, TwitchInput, User}
import org.mockito.Mockito.{times, verify, when}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.mockito.MockitoSugar.mock
import model.{Command, HiLoGame, TwitchInput}

class MlGameHandlerTest extends AnyWordSpec with Matchers {
  "A MlGameHandler object" should {
    val mlGameMock = mock[MlGame]
    val listMlGameMock = List(mlGameMock)
    val evaluatorMock = mock[Evaluator[HiLoGame]]
    val twitchInputMock = mock[TwitchInput]
    val userObject = User(userName = "TestUser", displayName = "TestUser", userId = "1234")
    val mlGameHandler = MlGameHandler(listMlGameMock, evaluatorMock)
    when(twitchInputMock.user).thenReturn(userObject)

    "start a game with a play command" in {
      val messageObject = Message(Array.empty, "play hilo", 123123, "123123")
      when(twitchInputMock.message).thenReturn(messageObject)
      mlGameHandler.handle(twitchInputMock)
      verify(twitchInputMock, times(2)).user
    }
    "ignore other commands" in {
      val messageObject = Message(Array.empty, "no play command", 123456789, "id-1234")
      when(twitchInputMock.message).thenReturn(messageObject)
      when(mlGameMock.handle(twitchInputMock)).thenReturn((Some(mlGameMock), Some("Testmessage")))
      mlGameHandler.handle(twitchInputMock) shouldBe a[(Command, Option[String])]
    }
    "start a game" in {
      mlGameHandler.startGame(twitchInputMock) shouldBe a[(Command, Option[String])]
    }
  }
}

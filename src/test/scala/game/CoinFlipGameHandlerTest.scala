package de.htwg.rs.chatbot
package game

import model.*

import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.{times, verify, when}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.mockito.MockitoSugar.mock

class CoinFlipGameHandlerTest extends AnyWordSpec with Matchers {

  "A CoinFlipGameHandler object" should {

    val twitchInputMock = mock[TwitchInput]
    val userObject = User(userName = "TestUser", displayName = "TestUser", userId = "1234")
    when(twitchInputMock.user).thenReturn(userObject)

    "start a game with a play command" in {

      val coinFlipGameHandler = CoinFlipGameHandler()
      val messageObject = Message(Array.empty, "play coinflip", 123123, "123123")
      when(twitchInputMock.message).thenReturn(messageObject)

      coinFlipGameHandler.handle(twitchInputMock)
      verify(twitchInputMock, times(2)).user
    }
    "ignore other commands" in {

      val coinFlipGameMock = mock[CoinFlipGame]
      val listCoinFlipMock = List(coinFlipGameMock)

      val coinFlipGameHandler = CoinFlipGameHandler(listCoinFlipMock)

      val messageObject = Message(Array.empty, "no play command", 123456789, "id-1234")
      when(twitchInputMock.message).thenReturn(messageObject)

      when(coinFlipGameMock.handle(twitchInputMock)).thenReturn((Some(coinFlipGameMock), Some("Testmessage")))

      coinFlipGameHandler.handle(twitchInputMock) shouldBe a[(Command, Option[String])]
    }
    "start a game" in {

      val coinFlipGameHandler = CoinFlipGameHandler()
      coinFlipGameHandler.startGame(twitchInputMock) shouldBe a[(Command, Option[String])]
    }
  }

}

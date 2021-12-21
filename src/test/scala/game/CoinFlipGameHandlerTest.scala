package de.htwg.rs.chatbot
package game

import de.htwg.rs.chatbot.model.{Channel, Message, TwitchInput, User}
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.{times, verify, when}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.mockito.MockitoSugar.mock

class CoinFlipGameHandlerTest extends AnyWordSpec with Matchers {

  val coinFlipGameHandler = CoinFlipGameHandler()
  val twitchInputMock = mock[TwitchInput]
  val userObject = User(userName = "TestUser", displayName = "TestUser", userId = "1234")
  when(twitchInputMock.user).thenReturn(userObject)


  "A CoinFlipGameHandlerTest object" should {
    "start a game with play" in {

      val playMessageObject = Message(Array.empty, "play", 123123, "123123")
      when(twitchInputMock.message).thenReturn(playMessageObject)

      coinFlipGameHandler.handle(twitchInputMock)
      verify(twitchInputMock,times(2)).user
    }
    "copy the existing instances to ignore other messages" in {

      val coinFlipGameMock = mock[CoinFlipGame]
      val coinFlipGameReal = CoinFlipGame(userObject)

      val listCoinFlipMock = List(coinFlipGameMock)

      val coinFlipGameHandler2 = CoinFlipGameHandler(listCoinFlipMock)


      val noPlayMessageObject = Message(Array.empty, "some message", 123123, "123123")
      when(twitchInputMock.message).thenReturn(noPlayMessageObject)

      when(coinFlipGameMock.handle(twitchInputMock)).thenReturn((Some(coinFlipGameMock), Some("hello pls")))

      coinFlipGameHandler2.handle(twitchInputMock)
    }
  }

}

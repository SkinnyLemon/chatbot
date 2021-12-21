package de.htwg.rs.chatbot
package game

import model.{Message, TwitchInput, User}

import org.mockito.Mockito.when
import org.scalatest.PrivateMethodTester
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.mockito.MockitoSugar.mock

class CoinFlipGameTest extends AnyWordSpec with Matchers with PrivateMethodTester {
  "A CoinFlipGame Object" should {
    val twitchInputMock = mock[TwitchInput]
    val userObject = User("Player1", "Player1", "id-1234")
    val coinFlipGame = CoinFlipGame(userObject)

    "handle the input for a existing user instance" in {
      val messageObject = Message(Array.empty, "h", 123123, "123123")
      when(twitchInputMock.message).thenReturn(messageObject)
      when(twitchInputMock.user).thenReturn(userObject)
      coinFlipGame.handle(twitchInputMock) shouldBe a[(Option[CoinFlipGame], Option[String])]
    }
    "ignore the input for other users" in {
      val messageObject = Message(Array.empty, "h", 123123, "123123")
      when(twitchInputMock.message).thenReturn(messageObject)
      val differentInputUser = User("Player2", "Player2", "id-4321")
      when(twitchInputMock.user).thenReturn(differentInputUser)
      coinFlipGame.handle(twitchInputMock) shouldBe a[(Option[CoinFlipGame], Option[String])]
    }
    "detect h or head as user input command" in {
      val messageObject = Message(Array.empty, "h", 123123, "123123")
      when(twitchInputMock.message).thenReturn(messageObject)
      val processInput = PrivateMethod[(Option[CoinFlipGame], Option[String])](Symbol("processInput"))
      coinFlipGame.invokePrivate(processInput(twitchInputMock)) shouldBe a[(Option[CoinFlipGame], Option[String])]
    }
    "detect t or tails as user input command" in {
      val messageObject = Message(Array.empty, "t", 123123, "123123")
      when(twitchInputMock.message).thenReturn(messageObject)
      val processInput = PrivateMethod[(Option[CoinFlipGame], Option[String])](Symbol("processInput"))
      coinFlipGame.invokePrivate(processInput(twitchInputMock)) shouldBe a[(Option[CoinFlipGame], Option[String])]
    }
    "detect s or score as user input command" in {
      val messageObject = Message(Array.empty, "s", 123123, "123123")
      when(twitchInputMock.message).thenReturn(messageObject)
      val processInput = PrivateMethod[(Option[CoinFlipGame], Option[String])](Symbol("processInput"))
      coinFlipGame.invokePrivate(processInput(twitchInputMock)) shouldBe a[(Option[CoinFlipGame], Option[String])]
    }
    "detect e or exit as user input command" in {
      val messageObject = Message(Array.empty, "e", 123123, "123123")
      when(twitchInputMock.message).thenReturn(messageObject)
      val processInput = PrivateMethod[(Option[CoinFlipGame], Option[String])](Symbol("processInput"))
      coinFlipGame.invokePrivate(processInput(twitchInputMock)) shouldBe a[(Option[CoinFlipGame], Option[String])]
    }
    "return the instance when no user command was given" in {
      val messageObject = Message(Array.empty, "no user command", 123123, "123123")
      when(twitchInputMock.message).thenReturn(messageObject)
      val processInput = PrivateMethod[(Option[CoinFlipGame], Option[String])](Symbol("processInput"))
      coinFlipGame.invokePrivate(processInput(twitchInputMock)) shouldBe a[(Option[CoinFlipGame], Option[String])]
    }
    "process a game" in {
      val processGame = PrivateMethod[(Option[CoinFlipGame], Option[String])](Symbol("processGame"))
      val choice = "h"
      coinFlipGame.invokePrivate(processGame(choice)) shouldBe a[(Option[CoinFlipGame], Option[String])]
    }
    "generate a result message" in {
      val generateMatchResultMessage = PrivateMethod[String](Symbol("generateMatchResultMessage"))
      val isWin = true
      coinFlipGame.invokePrivate(generateMatchResultMessage(isWin)) shouldBe a[String]
    }
    "increment or decrement the win/loss counter" in {
      val nextRound = PrivateMethod[CoinFlipGame](Symbol("nextRound"))
      val isWin = true
      coinFlipGame.invokePrivate(nextRound(isWin)) shouldBe a[CoinFlipGame]
    }
    "send back the end result" in {
      val sendResult = PrivateMethod[(Option[CoinFlipGame], Option[String])](Symbol("sendResult"))
      coinFlipGame.invokePrivate(sendResult()) shouldBe a[(Option[CoinFlipGame], Option[String])]
    }
    "generate a random head or tails value" in {
      val coinFlip = PrivateMethod[String](Symbol("coinFlip"))
      coinFlipGame.invokePrivate(coinFlip()) shouldBe a[String]
    }
    "end the game" in {
      val endGame = PrivateMethod[(Option[CoinFlipGame], Option[String])](Symbol("endGame"))
      coinFlipGame.invokePrivate(endGame()) shouldBe a[(Option[CoinFlipGame], Option[String])]
    }
  }
}

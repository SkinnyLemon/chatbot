package de.htwg.rs.chatbot
package game

import de.htwg.rs.chatbot.ai.Evaluator
import de.htwg.rs.chatbot.model.{HiLoGame, Message, TwitchInput, User}
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.{times, verify, when}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.mockito.MockitoSugar.mock

class MlGameTest extends AnyWordSpec with Matchers {

  "A MlGame object" should {
    val game = MLCore.startGame(7, 12) //TODO -> game mock to get rid of randomness
    val mLCoreObject = Running
    val twitchInputMock = mock[TwitchInput]
    val hiLoGameEvaluatorMock = mock[Evaluator[HiLoGame]]
    val userObject = User("Player1", "Player1", "id-1234")


    val mlGame = MlGame(userObject, game, hiLoGameEvaluatorMock)
    val messageObject = Message(Array.empty, "non valid command", 123123, "123123")
    when(twitchInputMock.message).thenReturn(messageObject)
    when(twitchInputMock.user).thenReturn(userObject)

    "handle the input" in {
      mlGame.handle(twitchInputMock)
      verify(twitchInputMock, times(1)).user
    }
    "return a touple of MLgame and optional String on handle" in {
      mlGame.handle(twitchInputMock) shouldBe a[(Option[MlGame], Option[String])]
    }

    "return a touple of MLgame and optional String on handle after an invalid user input" in {
      val invalidUserObject = User("Player1", "Player1", "not-the-same")
      val twitchInputMock = mock[TwitchInput]
      when(twitchInputMock.user).thenReturn(invalidUserObject)

      mlGame.handle(twitchInputMock) shouldBe a[(Option[MlGame], Option[String])]
    }

    "process the result on m oder h command" in {
      val twitchInputMock = mock[TwitchInput]
      val messageObject = Message(Array.empty, "h", 123123, "123123")
      when(twitchInputMock.message).thenReturn(messageObject)
      when(twitchInputMock.user).thenReturn(userObject)
      when(hiLoGameEvaluatorMock.evaluate(any())).thenReturn(0)

      mlGame.handle(twitchInputMock)
      verify(twitchInputMock, times(1)).user
    }
    
    //    "handle the input for a existing user instance" in {
//      val messageObject = Message(Array.empty, "h", 123123, "123123")
//      when(twitchInputMock.message).thenReturn(messageObject)
//      when(twitchInputMock.user).thenReturn(userObject)
//
//      mlGame.handle(twitchInputMock) shouldBe a[(Option[CoinFlipGame], Option[String])]
//    }
//
//    "ignore the input for other users" in {
//      val messageObject = Message(Array.empty, "h", 123123, "123123")
//      when(twitchInputMock.message).thenReturn(messageObject)
//
//      val differentInputUser = User("Player2", "Player2", "id-4321")
//      when(twitchInputMock.user).thenReturn(differentInputUser)
//
//      mlGame.handle(twitchInputMock) shouldBe a[(Option[CoinFlipGame], Option[String])]
//    }
//
//    "detect h or head as user input command" in {
//      val messageObject = Message(Array.empty, "h", 123123, "123123")
//      when(twitchInputMock.message).thenReturn(messageObject)
//
//      val processInput = PrivateMethod[(Option[CoinFlipGame], Option[String])](Symbol("processInput"))
//
//      mlGame.invokePrivate(processInput(twitchInputMock)) shouldBe a[(Option[CoinFlipGame], Option[String])]
//    }
//
//    "detect t or tails as user input command" in {
//      val messageObject = Message(Array.empty, "t", 123123, "123123")
//      when(twitchInputMock.message).thenReturn(messageObject)
//
//      val processInput = PrivateMethod[(Option[CoinFlipGame], Option[String])](Symbol("processInput"))
//
//      mlGame.invokePrivate(processInput(twitchInputMock)) shouldBe a[(Option[CoinFlipGame], Option[String])]
//    }
//
//    "detect s or score as user input command" in {
//      val messageObject = Message(Array.empty, "s", 123123, "123123")
//      when(twitchInputMock.message).thenReturn(messageObject)
//
//      val processInput = PrivateMethod[(Option[CoinFlipGame], Option[String])](Symbol("processInput"))
//
//      mlGame.invokePrivate(processInput(twitchInputMock)) shouldBe a[(Option[CoinFlipGame], Option[String])]
//    }
//
//    "detect e or exit as user input command" in {
//      val messageObject = Message(Array.empty, "e", 123123, "123123")
//      when(twitchInputMock.message).thenReturn(messageObject)
//
//      val processInput = PrivateMethod[(Option[CoinFlipGame], Option[String])](Symbol("processInput"))
//
//      mlGame.invokePrivate(processInput(twitchInputMock)) shouldBe a[(Option[CoinFlipGame], Option[String])]
//    }
//
//    "return the instance when no user command was given" in {
//      val messageObject = Message(Array.empty, "no user command", 123123, "123123")
//      when(twitchInputMock.message).thenReturn(messageObject)
//      val processInput = PrivateMethod[(Option[CoinFlipGame], Option[String])](Symbol("processInput"))
//
//      mlGame.invokePrivate(processInput(twitchInputMock)) shouldBe a[(Option[CoinFlipGame], Option[String])]
//    }
//
//    "process a game" in {
//      val processGame = PrivateMethod[(Option[CoinFlipGame], Option[String])](Symbol("processGame"))
//      val choice = "h"
//
//      mlGame.invokePrivate(processGame(choice)) shouldBe a[(Option[CoinFlipGame], Option[String])]
//    }
//
//    "generate a result message" in {
//      val generateMatchResultMessage = PrivateMethod[String](Symbol("generateMatchResultMessage"))
//      val isWin = true
//
//      mlGame.invokePrivate(generateMatchResultMessage(isWin)) shouldBe a[String]
//    }
//
//    "increment or decrement the win/loss counter" in {
//      val nextRound = PrivateMethod[CoinFlipGame](Symbol("nextRound"))
//      val isWin = true
//
//      mlGame.invokePrivate(nextRound(isWin)) shouldBe a[CoinFlipGame]
//    }
//
//    "send back the end result" in {
//      val sendResult = PrivateMethod[(Option[CoinFlipGame], Option[String])](Symbol("sendResult"))
//
//      mlGame.invokePrivate(sendResult()) shouldBe a[(Option[CoinFlipGame], Option[String])]
//    }
//
//    "generate a random head or tails value" in {
//      val coinFlip = PrivateMethod[String](Symbol("coinFlip"))
//
//      mlGame.invokePrivate(coinFlip()) shouldBe a[String]
//    }
//
//    "end the game" in {
//      val endGame = PrivateMethod[(Option[CoinFlipGame], Option[String])](Symbol("endGame"))
//
//      mlGame.invokePrivate(endGame()) shouldBe a[(Option[CoinFlipGame], Option[String])]
//    }

  }
}

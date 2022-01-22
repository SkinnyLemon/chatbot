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
    val winMock = mock[Win]
    val lossMock = mock[Loss]
    val gameMock = mock[Running]

    val anotherGameMock = mock[Running]
    when(anotherGameMock.discardPile).thenReturn(List(0, 0, 0, 0, 0))

    val twitchInputMock = mock[TwitchInput]
    val hiLoGameEvaluatorMock = mock[Evaluator[HiLoGame]]
    val userObject = User("Player1", "Player1", "id-1234")
    val mlGame = MlGame(userObject, gameMock, hiLoGameEvaluatorMock)
    val messageObject = Message(Array.empty, "non valid command", 123123, "123123")
    setupTwitchInputMock(userObject, twitchInputMock, messageObject)

    "handle the input" in {
      mlGame.handle(twitchInputMock)
      verify(twitchInputMock, times(1)).user
    }
    "return a touple of MLgame and optional String on handle" in {
      mlGame.handle(twitchInputMock) shouldBe a[(Option[MlGame], Option[String])]
    }

    "return a touple of MLgame and optional String on handle after an invalid user input" in {
      val invalidUserObject = User("Player1", "Player1", "not-the-same")
      when(twitchInputMock.user).thenReturn(invalidUserObject)
      mlGame.handle(twitchInputMock) shouldBe a[(Option[MlGame], Option[String])]
    }

    "process the result" in {
      val twitchInputMock = mock[TwitchInput]
      setupMessageAndTwitchInputMock(userObject, twitchInputMock)
      setupAiMockToRecommendUp(hiLoGameEvaluatorMock)
      setupGameMockBets(gameMock, winMock)

      mlGame.handle(twitchInputMock)
      verify(twitchInputMock, times(1)).user
    }


    "generate player wins game over message on game over" in {
      setupMessageAndTwitchInputMock(userObject, twitchInputMock)
      setupGameMockBets(gameMock, winMock)
      setupAiMockToRecommendUp(hiLoGameEvaluatorMock)
      val output = mlGame.handle(twitchInputMock)

      output._1 shouldBe None
      output._2.get shouldBe "Correct! Game Over, you win. Card was a 0. You got a streak of 0"
    }


    "generate player wrong guess message on wrong player guess " in {
      setupMessageAndTwitchInputMock(userObject, twitchInputMock)
      setupGameMockBets(gameMock, lossMock)
      setupAiMockToRecommendUp(hiLoGameEvaluatorMock)
      val output = mlGame.handle(twitchInputMock)

      output._1 shouldBe None
      output._2.get shouldBe "Too bad, you loose! The card drawn was a 0. You got a streak of 0."
    }

    "generate ai loss message when ai lost " in {
      setupMessagAndTwitchMocksForContinuesGame(gameMock, anotherGameMock, twitchInputMock, userObject)
      setupGameMockBets(anotherGameMock, lossMock)
      when(hiLoGameEvaluatorMock.evaluate(any())).thenReturn(HiLoGame(0, 0, 0, 0, 0, 0))
      val output = mlGame.handle(twitchInputMock)

      output._1 shouldBe None
      output._2.get shouldBe "Correct! next card was a 0. Ai guessed wrong and has drawn a: 0 => Game over. Player wins! Streak: 0"
    }


    "generate ai win message when ai finishes the game " in {
      setupMessagAndTwitchMocksForContinuesGame(gameMock, anotherGameMock, twitchInputMock, userObject)
      setupGameMockBets(anotherGameMock, winMock)
      when(hiLoGameEvaluatorMock.evaluate(any())).thenReturn(HiLoGame(0, 0, 0, 0, 0, 0))
      val output = mlGame.handle(twitchInputMock)

      output._1 shouldBe None
      output._2.get shouldBe "Correct! next card was a 0. Ai guessed correct and has drawn a: 0 => Game over. Both win! Streak: 0"
    }

    "generate game running message when game continues " in {
      setupMessagAndTwitchMocksForContinuesGame(gameMock, anotherGameMock, twitchInputMock, userObject)
      setupGameMockBets(anotherGameMock, anotherGameMock)
      setupAiMockToRecommendUp(hiLoGameEvaluatorMock)
      val output = mlGame.handle(twitchInputMock)

      output._1.get shouldBe MlGame(userObject, anotherGameMock, hiLoGameEvaluatorMock)
      output._2.get shouldBe "Correct! next card was a 0. Ai had a correct guess for the card drawn: 0. The game is still going. Current Streak: 0. Will you go (h)igher or (l)ower than 0?"
    }
  }

  private def setupMessagAndTwitchMocksForContinuesGame(gameMock: Running, anotherGameMock: Running, twitchInputMock: TwitchInput, userObject: User) =
    setupMessageAndTwitchInputMock(userObject, twitchInputMock)
    setupGameMockBets(gameMock, anotherGameMock)


  private def setupGameMockBets(gameMock: Running, toReturn: Win | Loss | Running) =
    when(gameMock.betMore).thenReturn(toReturn)
    when(gameMock.betLess).thenReturn(toReturn)

  private def setupMessageAndTwitchInputMock(userObject: User, twitchInputMock: TwitchInput) =
    val messageObject = Message(Array.empty, "h", 123123, "123123")
    setupTwitchInputMock(userObject, twitchInputMock, messageObject)

  private def setupAiMockToRecommendUp(hiLoGameEvaluatorMock: Evaluator[HiLoGame]) =
    when(hiLoGameEvaluatorMock.evaluate(any())).thenReturn(HiLoGame(0, 0, 0, 0, 0, 1))

  private def setupTwitchInputMock(userObject: User, twitchInputMock: TwitchInput, messageObject: Message) =
    when(twitchInputMock.message).thenReturn(messageObject)
    when(twitchInputMock.user).thenReturn(userObject)


}

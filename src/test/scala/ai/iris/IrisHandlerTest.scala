package de.htwg.rs.chatbot
package ai.iris

import de.htwg.rs.chatbot.ai.{Classifier, Evaluator}
import model.*

import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.{times, verify, when}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.mockito.MockitoSugar.mock

class IrisHandlerTest extends AnyWordSpec with Matchers {

  "A IrisHandler object " should {

    val twitchInputMock = mock[TwitchInput]
    val userObject = User(userName = "TestUser", displayName = "TestUser", userId = "1234")
    when(twitchInputMock.user).thenReturn(userObject)

    val irisEvaluator: Evaluator[Iris] = IrisClassifier().initialize()
    val irisHandler = IrisHandler(irisEvaluator)

    "start a prediction via command" in {

      val messageObject = Message(Array.empty, "predict iris", 123123, "123123")
      when(twitchInputMock.message).thenReturn(messageObject)

      irisHandler.handle(twitchInputMock)
      verify(twitchInputMock, times(1)).message
    }

   "execute a prediction properly" in {
     val messageStringToPredict = "predict iris 6.5,2.8,5.6,2.1"
     irisHandler.executePrediction(messageStringToPredict)
   }
  }
}

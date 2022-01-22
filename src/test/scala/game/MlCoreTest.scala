package de.htwg.rs.chatbot
package game

import de.htwg.rs.chatbot.ai.Evaluator
import de.htwg.rs.chatbot.model.{HiLoGame, Message, TwitchInput, User}
import org.mockito.Mockito.{times, verify, when}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.mockito.MockitoSugar.mock

class MlCoreTest extends AnyWordSpec with Matchers {


  "A MLCore object" should {
    val core = MLCore
    "return a running game on start" in {
      core.startGame(7, 12) shouldBe a[Running]
    }
  }


  "A MLCore running game" should {
    val core = MLCore
    val game = MLCore.startGame(0, 1)

    "be be lost when first drawn card is high and won if low" in {
      val result = game.betMore
      if(game.current == 1) {
        game.betMore shouldBe a[Loss]
      } else {
        game.betMore shouldBe a[Win]
      }
    }

  }

}

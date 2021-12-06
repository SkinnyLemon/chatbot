package de.htwg.rs.chatbot
package control

import io.TwitchOutput

import de.htwg.rs.chatbot.model.{DefaultCommand, PrefixTrigger, Trigger}
import net.bytebuddy.description.`type`.TypeList.Generic.Empty
import org.mockito.Mockito.{times, verify, when}
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.mockito.MockitoSugar.mock


class CommandDslTest extends AnyWordSpec with Matchers {

  val fixObject = "fixObject"
  val response = "response"

  "A whenMessage object" should {
    "return trigger defined for all its methods" in {
      val startsWithTrigger = `when message` `starts with` (fixObject)
      val containsTrigger = `when message` contains (fixObject)
      val isTrigger = `when message` is (fixObject)
      val suffixTrigger = `when message` `ends with` (fixObject)

      startsWithTrigger shouldBe a[TriggerDefined]
      containsTrigger shouldBe a[TriggerDefined]
      isTrigger shouldBe a[TriggerDefined]
      suffixTrigger shouldBe a[TriggerDefined]
    }
  }

  "A triggerDefined object" should {
    "return a default command" in {
      val prefixTrigger = new PrefixTrigger(fixObject)
      val defined = new TriggerDefined(prefixTrigger)
      defined.trigger shouldBe prefixTrigger
    }
  }

}

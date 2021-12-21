package de.htwg.rs.chatbot
package model

import control.ChannelOutput
import io.TwitchOutput

import org.mockito.Mockito.when
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.mockito.MockitoSugar.mock


class CommandTest extends AnyWordSpec with Matchers {
  val channelOutputMock = mock[ChannelOutput]
  val commandMock = mock[Command]
  val twitchInputMock = mock[TwitchInput]
  val commandRegistry = new CommandRegistry(channelOutputMock)
  val fixOperator = "!create"
  val myFixMessage = Message(Array.empty, fixOperator, 123123, "myId")
  when(twitchInputMock.message) thenReturn (myFixMessage)

  "A PrefixTrigger" should {
    val trigger = new PrefixTrigger(fixOperator)
    "return a boolean" in {
      trigger.isTriggered(twitchInputMock) shouldBe a[Boolean]
    }
  }
  "A SuffixTrigger" should {
    val trigger = new SuffixTrigger(fixOperator)
    "return a boolean" in {
      trigger.isTriggered(twitchInputMock) shouldBe a[Boolean]
    }
  }
  "A ContainsTrigger" should {
    val trigger = new ContainsTrigger(fixOperator)
    "return a boolean" in {
      trigger.isTriggered(twitchInputMock) shouldBe a[Boolean]
    }
  }
  "A EqualsTrigger" should {
    val trigger: EqualsTrigger = new EqualsTrigger(fixOperator)
    "return a boolean" in {
      trigger.isTriggered(twitchInputMock) shouldBe a[Boolean]
    }
    "exists" in {
      trigger.toEqual shouldBe fixOperator
    }
  }
  "A BasicResponseGenerator" should {
    val basicResponseGenerator = new BasicResponseGenerator("test response")
    "return a response" in {
      basicResponseGenerator.generateResponse(twitchInputMock) shouldBe "test response"
    }
  }
  "A DefaultCommand" should {
    val basicResponseGenerator = new BasicResponseGenerator("test response")
    val mockTrigger = mock[Trigger]
    val defaultCommand = new DefaultCommand(mockTrigger, basicResponseGenerator)

    "return a tuple with the object iteself some option" in {
      when(mockTrigger.isTriggered(twitchInputMock)) thenReturn true
      defaultCommand.handle(twitchInputMock)._2.get shouldBe "test response"
    }
    "return a tuple with the object iteself and none" in {
      when(mockTrigger.isTriggered(twitchInputMock)) thenReturn false
      defaultCommand.handle(twitchInputMock)._2 shouldBe None
    }
  }
}

package de.htwg.rs.chatbot
package io

import control.ChannelOutput
import io.TwitchOutput

import de.htwg.rs.chatbot.io
import org.mockito.Captor
import org.mockito.Mockito.{spy, verify, when}
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.mockito.MockitoSugar.mock


class TwitchConnectionTest extends AnyWordSpec with Matchers {
  val bot = Config.bots.head
  val twitchConnection = TwitchConnection.establishConnection(bot.name, bot.auth)
  val twitchConnectionImpl = new TwitchConnectionImpl(bot.name, bot.auth)
  val consumerMock = mock[TwitchConsumer]
  val channelName = "imperiabot"

  "A TwitchConnection" should {
    "retrun a twitchConnectiuon on establishing a connection" in {
      TwitchConnection.establishConnection(bot.name, bot.auth) shouldBe a[TwitchConnection]
    }
    "return a raw inputprovider on getInput" in {
      twitchConnection.getInput shouldBe a[RawInputProvider]
    }
    "return a output on getOutput" in {
      twitchConnection.getOutput shouldBe a[TwitchOutput]
    }
    "run on start" in {
      twitchConnection.start() shouldBe a[Unit]
    }
    "send a message on join" in {
      twitchConnection.join("imperiabot")
    }
    "send a message on sendMessage" in {
      twitchConnectionImpl.sendMessage("imperiabot", "msg") shouldBe a[Unit]
    }
    "send a message on sendMessage with tags" in {
      val tags = Map("AL" -> "Alabama", "AK" -> "Alaska")
      twitchConnectionImpl.sendMessage("imperiabot", "msg", tags) shouldBe a[Unit]
    }
    "be able to add and remove something from the subscribers list without craching" in {
      twitchConnectionImpl.subscribe(consumerMock) shouldBe a[Unit]
      twitchConnectionImpl.unSubscribe(consumerMock) shouldBe a[Unit]
    }
  }
}

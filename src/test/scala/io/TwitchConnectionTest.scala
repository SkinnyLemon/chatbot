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


  }

}

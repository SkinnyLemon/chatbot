package de.htwg.rs.chatbot
package control

import control.ChannelOutputTest
import io.TwitchOutput

import net.bytebuddy.description.`type`.TypeList.Generic.Empty
import org.mockito.Mockito.{times, verify, when}
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.mockito.MockitoSugar.mock


class ChannelOutputTest extends AnyWordSpec with Matchers {

  val channelName = "imperiabot"
  val message = "msg"

  val outputMock = mock[TwitchOutput]
  val channelOutput = new ChannelOutput(outputMock, channelName)

  "A ChannelOutput" should {
    "send a message on send" in {
      channelOutput.send(message)
      verify(outputMock, times(1)).sendMessage(channelName, message, Map.empty)
    }

  }


}

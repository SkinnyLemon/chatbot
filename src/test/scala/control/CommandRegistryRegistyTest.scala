package de.htwg.rs.chatbot
package control

import de.htwg.rs.chatbot.io.TwitchOutput
import de.htwg.rs.chatbot.model.{Channel, Command, Message, TwitchInput}
import org.mockito.Mockito.{times, verify, when}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.mockito.MockitoSugar.mock

class CommandRegistryRegistyTest extends AnyWordSpec with Matchers {

  val channelObject = new Channel("123", "name")
  val messageObject = new Message(Array.empty, "text", 123123, "123123")
  val inputMock = mock[TwitchInput]
  when(inputMock.channel) thenReturn(channelObject)
  when(inputMock.message) thenReturn(messageObject)

  val outputMock = mock[TwitchOutput]
  val commandMock = mock[Command]
  val commandRegistryRegisty = new CommandRegistryRegisty(outputMock)
  val channel = "imperiabot"


  "A CommandRegistryRegistyTest object" should {
    "add a command" in {
      commandRegistryRegisty.addCommand(channel, commandMock)
    }

    "remove a command in" in {
      commandRegistryRegisty.removeCommand(channel, commandMock)
    }

    "execute handle message on command" in {
      commandRegistryRegisty.handleMessage(inputMock)
    }
  }


}

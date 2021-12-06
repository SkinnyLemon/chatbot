package de.htwg.rs.chatbot
package model

import control.{ChannelOutput, ChannelOutputTest}
import io.TwitchOutput

import org.mockito.Mockito.when
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.mockito.MockitoSugar.mock


class CommandRegistryTest extends AnyWordSpec with Matchers {

  val channelOutputMock = mock[ChannelOutput]
  val commandMock = mock[Command]
  val twitchInputMock = mock[TwitchInput]

  val commandRegistry = new CommandRegistry(channelOutputMock)


  "A CommandRegistry" should {

    "return itself" in {
      commandRegistry.addCommand(commandMock) shouldBe a[CommandRegistry]
    }

    "add a command" in {
      commandRegistry.addCommand(commandMock).commands should not be empty
    }

    "remove a command" in {
      commandRegistry.addCommand(commandMock).removeCommand(commandMock).commands shouldBe empty
    }

    "return itself when !create is present" in {
      val myMessage = Message(Array.empty, "!create myCommand", 123123, "myId")
      val myOption: Option[String] = None

      when(commandMock.handle(twitchInputMock)) thenReturn ((commandMock, myOption))
      when(twitchInputMock.message) thenReturn (myMessage)

      commandRegistry.addCommand(commandMock).handleMessage(twitchInputMock) shouldBe a[CommandRegistry]
    }

    "return itself when !create is not present" in {
      val myMessage = Message(Array.empty, "123123", 123123, "myId")
      val myOption: Option[String] = None

      when(commandMock.handle(twitchInputMock)) thenReturn ((commandMock, myOption))
      when(twitchInputMock.message) thenReturn (myMessage)

      commandRegistry.addCommand(commandMock).handleMessage(twitchInputMock) shouldBe a[CommandRegistry]
    }


    "return itself when command is valid" in {
      val myMessage = Message(Array.empty, "!create when message is \"foo\" respond with \"bar\"", 123123, "myId")
      val myOption: Option[String] = None

      when(commandMock.handle(twitchInputMock)) thenReturn ((commandMock, myOption))
      when(twitchInputMock.message) thenReturn (myMessage)

      commandRegistry.addCommand(commandMock).handleMessage(twitchInputMock) shouldBe a[CommandRegistry]
    }


  }


}

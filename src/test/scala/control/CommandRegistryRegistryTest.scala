//package de.htwg.rs.chatbot
//package control
//
//import io.TwitchOutput
//import model.{Command, DefaultCommand, PrefixTrigger, Trigger}
//
//import net.bytebuddy.description.`type`.TypeList.Generic.Empty
//import org.mockito.Mockito.{times, verify, when}
//import org.scalatest.funsuite.AnyFunSuite
//import org.scalatest.matchers.should.Matchers
//import org.scalatest.wordspec.AnyWordSpec
//import org.scalatestplus.mockito.MockitoSugar.mock
//
//
//class CommandRegistryRegistryTest extends AnyWordSpec with Matchers {
//
//  val channelName = "imperiabot"
//  val outputMock = mock[TwitchOutput]
//  val commandMock = mock[Command]
//  val commandRegistryRegisty = new CommandRegistryRegisty(outputMock)
//
//  "A CommandRegistryRegisty object" should {
//    "add commands" in {
//      commandRegistryRegisty.addCommand(channelName, commandMock) shouldBe a[Unit]
//    }
//  }
//
//}

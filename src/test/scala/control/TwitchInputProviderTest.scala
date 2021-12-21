package de.htwg.rs.chatbot
package control

import io.{TwitchConnection, TwitchOutput}
import model.{Channel, Command, Message, TwitchInput, TwitchInputParser}

import org.mockito.Mockito.{times, verify, when}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.mockito.MockitoSugar.mock

import scala.collection.mutable.ListBuffer
import scala.util.Success

class TwitchInputProviderTest extends AnyWordSpec with Matchers {

  val outPutMock = mock[TwitchOutput]
  val connectionMock = mock[TwitchConnection]
  val registryMock = mock[CommandRegistryRegisty]

  val faultyMessage = "PRIVMSG faulty"
  val rawMessage = "@badge-info=;badges=;client-nonce=624f247e40cfba1d93ff8b213cd3433b;color=;display-name=omrisswk;emotes=;first-msg=0;flags=;id=5512d983-c07d-444e-8f1b-4e5620a5461a;mod=0;room-id=735101247;subscriber=0;tmi-sent-ts=1635690689871;turbo=0;user-id=737708557;user-type= :omrisswk!omrisswk@omrisswk.tmi.twitch.tv PRIVMSG #imperiabot :asd\nTwitchInput(Channel(735101247,imperiabot),User(omrisswk,omrisswk,737708557),Message([Lde.htwg.rs.chatbot.model.Emote;@15aee408,asd,1635690689871,5512d983-c07d-444e-8f1b-4e5620a5461a))"


  val provider = new TwitchInputProvider(outPutMock, connectionMock, registryMock)
  val parser = new TwitchInputParser()
  val message = parser.parseToTwitchInput(rawMessage) match {
    case Success(input) => input
  }


  "A TwitchInputprovider" should {
    "not do anything if message is faulty" in {
      provider.onMessage(faultyMessage)
    }

    "carry on to hanlde the message" in {
      provider.onMessage(rawMessage)

      verify(registryMock, times(1))
    }

  }


}

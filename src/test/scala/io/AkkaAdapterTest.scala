package de.htwg.rs.chatbot
package io

import control.{ChannelOutput, CommandRegistryRegisty}
import io.TwitchOutput

import akka.stream.BoundedSourceQueue
import net.bytebuddy.description.`type`.TypeList.Generic.Empty
import org.mockito.Mockito.{doNothing, spy, times, verify, when}
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.mockito.MockitoSugar.mock
import akka.stream.scaladsl.{Flow, Sink, Source}
import de.htwg.rs.chatbot.model.TwitchInputParser


class AkkaAdapterTest extends AnyWordSpec with Matchers {

  val connectionMock = mock[TwitchConnection]
  val parser = new TwitchInputParser
  val akkaAdapter = new AkkaAdapter(Config.bots.head)
  val registry = new CommandRegistryRegisty(akkaAdapter.connection.getOutput)


  val spiedAdapter = spy(akkaAdapter)
  when(spiedAdapter.connection).thenReturn(connectionMock)


  val processingSink = Flow[String]
    .map(parser.parseToTwitchInput)
    .filter(_.isSuccess)
    .map(_.get)
    .groupBy(Int.MaxValue, _.channel.name)
    .to(Sink.foreach(registry.handleMessage))


  "A Akka adapter should call start on a sink" in {
    spiedAdapter.start(processingSink)
    verify(connectionMock, times(1)).start()
  }

}
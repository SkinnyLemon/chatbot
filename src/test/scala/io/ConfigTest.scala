package de.htwg.rs.chatbot
package io

import control.ChannelOutput
import io.TwitchOutput

import net.bytebuddy.description.`type`.TypeList.Generic.Empty
import org.mockito.Mockito.when
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.mockito.MockitoSugar.mock


class ConfigTest extends AnyWordSpec with Matchers {
  "A Config" should {
    "hold bots" in {
      Config.bots should not be empty
    }
  }
}

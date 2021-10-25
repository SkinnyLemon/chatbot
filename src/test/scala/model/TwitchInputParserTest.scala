package de.htwg.rs.chatbot
package model

import org.scalatest.*
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class TwitchInputParserTest extends AnyWordSpec with Matchers {

  val tagsMap = Map(("room-id", "1337"), ("display-name", "Ronni"), ("emotes", "25:0-4,12-16/1902:6-10"), ("tmi-sent-ts", "1507246572675"), ("user-id", "1234"), ("color", "#0D4200"),
    ("badges", "#global_mod/1,turbo/1"), ("id", "b34ccfc7-4977-403a-8a94-33c6bac34fb8"), ("badge-info", ""), ("mod", "0"), ("subscriber", "0"), ("turbo", "1"), ("user-type", "global_mod"))

  "A channel parser" should {
    val parser = new ChannelParser()
    val channelName = "MyTwitchChannelName"
    val channelTry = parser.parseToChannel(tagsMap, channelName)

    "parse a valid input" in {
      channelTry.isSuccess shouldBe true
    }

    val channel = channelTry.get
    "return a channel object" in {
      channel shouldBe a[Channel]
    }
    "create an object with the desired name" in {
      channel.name should be(channelName) // @ Tobi, gibt es hier eine best practise wegen String?
    }
    "hold the roomId as it passed in the tags" in {
      channel.roomId should be(tagsMap("room-id"))
    }
  }

  "A user parser" should {
    val parser = new UserParser()
    val userName = "RonnieRonaldo"
    val userTry = parser.parseToUser(tagsMap, userName)

    "parse a valid input" in {
      userTry.isSuccess shouldBe true
    }

    val user = userTry.get
    "return a user object" in {
      user shouldBe a[User]
    }
    "create an user with the desired username" in {
      user.userName should be(userName)
    }
    "create an user with the desired display name" in {
      user.displayName should be(tagsMap("display-name"))
    }
    "hold the userId as it is passed in the tags" in {
      user.userId should be(tagsMap("user-id"))
    }
  }

  "A Message parser" should {
    val parser = new MessageParser()
    val msg = "kappa"
    val messageTry = parser.parseToMessage(tagsMap, msg)

    "parse a valid input" in {
      messageTry.isSuccess shouldBe true
    }

    val message = messageTry.get
    "return a message object" in {
      message shouldBe a[Message]
    }
    "hold a message" in {
      message.text should be(msg)
    }
    "have a timestamp of type Long" in {
      message.timeStamp shouldBe a[Long]
    }
    "hold an array of emotes" in {
      message.emotes(0) shouldBe a[Emote]
    }
  }
}

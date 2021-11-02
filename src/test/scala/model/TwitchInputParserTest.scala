package de.htwg.rs.chatbot
package model

import org.scalatest.*
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class TwitchInputParserTest extends AnyWordSpec with Matchers {

  val tagsMap = Map(("room-id", "1337"), ("display-name", "Ronni"), ("emotes", "25:0-4,12-16/1902:6-10"), ("tmi-sent-ts", "1507246572675"), ("user-id", "1234"), ("color", "#0D4200"),
    ("badges", "#global_mod/1,turbo/1"), ("id", "b34ccfc7-4977-403a-8a94-33c6bac34fb8"), ("badge-info", ""), ("mod", "0"), ("subscriber", "0"), ("turbo", "1"), ("user-type", "global_mod"))

  val faultyEmotesTagsMap = Map(("room-id", "1337"), ("display-name", "Ronni"), ("emotes", "25:0-4,12-16/1902:6-"), ("tmi-sent-ts", "1507246572675"), ("user-id", "1234"), ("color", "#0D4200"),
    ("badges", "#global_mod/1,turbo/1"), ("id", "b34ccfc7-4977-403a-8a94-33c6bac34fb8"), ("badge-info", ""), ("mod", "0"), ("subscriber", "0"), ("turbo", "1"), ("user-type", "global_mod"))

  val tagsMapWithoutDisplayName = tagsMap.filter((k, v) => k != "display-name")
  val tagsMapWithoutTimeStamp = tagsMap.filter((k, v) => k != "tmi-sent-ts")


  val roomIdPart = ""
  val rawMessage = "@badge-info=;badges=;client-nonce=624f247e40cfba1d93ff8b213cd3433b;color=;display-name=omrisswk;emotes=;first-msg=0;flags=;id=5512d983-c07d-444e-8f1b-4e5620a5461a;mod=0;room-id=735101247;subscriber=0;tmi-sent-ts=1635690689871;turbo=0;user-id=737708557;user-type= :omrisswk!omrisswk@omrisswk.tmi.twitch.tv PRIVMSG #imperiabot :asd\nTwitchInput(Channel(735101247,imperiabot),User(omrisswk,omrisswk,737708557),Message([Lde.htwg.rs.chatbot.model.Emote;@15aee408,asd,1635690689871,5512d983-c07d-444e-8f1b-4e5620a5461a))"
  val rawMessageWithInvalidRoomId = "@badge-info=;badges=;client-nonce=624f247e40cfba1d93ff8b213cd3433b;color=;display-name=omrisswk;emotes=;first-msg=0;flags=;id=5512d983-c07d-444e-8f1b-4e5620a5461a;mod=0;subscriber=0;tmi-sent-ts=1635690689871;turbo=0;user-id=737708557;user-type= :omrisswk!omrisswk@omrisswk.tmi.twitch.tv PRIVMSG #imperiabot :asd\nTwitchInput(Channel(735101247,imperiabot),User(omrisswk,omrisswk,737708557),Message([Lde.htwg.rs.chatbot.model.Emote;@15aee408,asd,1635690689871,5512d983-c07d-444e-8f1b-4e5620a5461a))"
  val rawMessageWithNoIdInTags = "@badge-info=;badges=;client-nonce=624f247e40cfba1d93ff8b213cd3433b;color=;display-name=omrisswk;emotes=;first-msg=0;flags=;mod=0;room-id=735101247;subscriber=0;tmi-sent-ts=1635690689871;turbo=0;user-id=737708557;user-type= :omrisswk!omrisswk@omrisswk.tmi.twitch.tv PRIVMSG #imperiabot :asd\nTwitchInput(Channel(735101247,imperiabot),User(omrisswk,omrisswk,737708557),Message([Lde.htwg.rs.chatbot.model.Emote;@15aee408,asd,1635690689871,5512d983-c07d-444e-8f1b-4e5620a5461a))"
  val rawMessageWithNoUserIdInTags = "@badge-info=;badges=;client-nonce=624f247e40cfba1d93ff8b213cd3433b;color=;display-name=omrisswk;emotes=;first-msg=0;flags=;id=5512d983-c07d-444e-8f1b-4e5620a5461a;mod=0;room-id=735101247;subscriber=0;tmi-sent-ts=1635690689871;turbo=0;user-type= :omrisswk!omrisswk@omrisswk.tmi.twitch.tv PRIVMSG #imperiabot :asd\nTwitchInput(Channel(735101247,imperiabot),User(omrisswk,omrisswk,737708557),Message([Lde.htwg.rs.chatbot.model.Emote;@15aee408,asd,1635690689871,5512d983-c07d-444e-8f1b-4e5620a5461a))"
  val rawMessageWithfaultyTagString = "@ba=dge=-info=;badges=;client-nonce=624f247e40cfba1d93ff8b213cd3433b;color=;display-name=omrisswk;emotes=;first-msg=0;flags=;id=5512d983-c07d-444e-8f1b-4e5620a5461a;mod=0;room-id=735101247;subscriber=0;tmi-sent-ts=1635690689871;turbo=0;user-type= :omrisswk!omrisswk@omrisswk.tmi.twitch.tv PRIVMSG #imperiabot :asd\nTwitchInput(Channel(735101247,imperiabot),User(omrisswk,omrisswk,737708557),Message([Lde.htwg.rs.chatbot.model.Emote;@15aee408,asd,1635690689871,5512d983-c07d-444e-8f1b-4e5620a5461a))"


  "A Twitch input parser" should {
    val parser = new TwitchInputParser()

    "succseed with parsing on a correct inpit string" in {
      parser.parseToTwitchInput(rawMessage).isSuccess shouldBe true
    }

    "fail to parse when input was wrong" in {
      parser.parseToTwitchInput("wrong string").isFailure shouldBe true
    }

    "fail to parse when channel parser fails" in {
      parser.parseToTwitchInput(rawMessageWithInvalidRoomId).isFailure shouldBe true
    }

    "fail to parse when message parser  fails" in {
      parser.parseToTwitchInput(rawMessageWithNoIdInTags).isFailure shouldBe true
    }

    "fail to parse when user parser fails" in {
      parser.parseToTwitchInput(rawMessageWithNoUserIdInTags).isFailure shouldBe true
    }

    "fail to parse when tag string is faulty" in {
      parser.parseToTwitchInput(rawMessageWithfaultyTagString).isFailure shouldBe true
    }

  }


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
      channel.name should be(channelName)
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

    "return a user name, when no display name is available" in {
      val userTryWithoutDisplayName = parser.parseToUser(tagsMapWithoutDisplayName, userName)
      userTryWithoutDisplayName.get.displayName should be(userName)
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

    "return a failiure, when there is no timestamp in map" in {
      val messageTry = parser.parseToMessage(tagsMapWithoutTimeStamp, msg)
      messageTry.isFailure shouldBe true
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

    "fail when the emote array is wrong" in {
      val messageTry = parser.parseToMessage(faultyEmotesTagsMap, msg)
      messageTry.get.emotes.length <= 2

    }

  }
}

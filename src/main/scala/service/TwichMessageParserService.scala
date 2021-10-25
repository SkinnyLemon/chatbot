package de.htwg.rs.chatbot.service

import de.htwg.rs.chatbot.model.*

import scala.util.{Failure, Success, Try}

class TwitchInputParser:
  val channelParser = new ChannelParser()
  val userParser = new UserParser()
  val messageParser = new MessageParser()
  
  def parseToTwitchInput(toParse: String): Try[TwitchInput] = {
    val split = toParse.split(" ")
    if (split.length < 5)
      return Failure(new IllegalArgumentException("Input doesnt match expected format"))
    val rawTags = split(0).drop(1)
    val userName = split(1).drop(1).takeWhile(_ != '!')
    val channelName = split(3).drop(1)
    val rawMessage = split.drop(4).mkString(" ").drop(1)

    val tagMap = parseTags(rawTags)

    channelParser.parseToChannel(tagMap, channelName) match {
      case Success(channel) => userParser.parseToUser(tagMap, userName) match {
        case Success(user) => messageParser.parseToMessage(tagMap, rawMessage) match {
          case Success(message) => Success(TwitchInput(channel, user, message))
          case Failure(error) => Failure(error)
        }
        case Failure(error) => Failure(error)
      }
      case Failure(error) => Failure(error)
    }
  }

  private def parseTags(toParse: String): Map[String, String] = toParse
    .split(";")
    .map(_.split('='))
    .map {
      case Array(k: String, v: String) => Some((k, v))
      case Array(k: String) => None
      case a =>
        new IllegalArgumentException(s"bad array: ${a.mkString(", ")}").printStackTrace()
        None
    }
    .filter(_.isDefined)
    .map(_.get)
    .toMap

class ChannelParser:
  def parseToChannel(tags: Map[String, String], channelName: String): Try[Channel] =
    Try(tags("room-id")) match {
      case Success(roomId) => Success(Channel(roomId, channelName))
      case _ => Failure(new IllegalArgumentException("No room-id in tags"))
    }

class UserParser:
  def parseToUser(tags: Map[String, String], userName: String): Try[User] =
    val displayName = Try(tags("display-name")) match {
      case Success(s) => s
      case _ => userName
    }
    Try(tags("user-id")) match {
      case Success(userId) => Success(User(userName, displayName, userId))
      case _ => Failure(new IllegalArgumentException("No user-id in tags"))
    }

class MessageParser:
  def parseToMessage(tags: Map[String, String], message: String): Try[Message] =
    val emotes = Try(tags("emotes")) match {
      case Success(rawEmotes) => rawEmotes.split("/").flatMap(parseToEmote)
      case _ => Array.empty[Emote]
    }
    Try(tags("tmi-sent-ts")) match {
      case Success(timeStamp) => Success(Message(emotes, message, timeStamp.toLong))
      case _ => Failure(new IllegalArgumentException("No tmi-sent-ts in tags"))
    }

  private def parseToEmote(toParse: String): Array[Emote] =
    val (emoteId: String, indicesRaw: String) = toParse.span(_ != ':')
    indicesRaw.drop(1).split(",")
      .map(_.split("-"))
      .map(s => Try(Emote(emoteId, s(0).toInt, s(1).toInt)))
      .filter {
        case Success(_) =>
          true
        case Failure(e) =>
          e.printStackTrace()
          false
      }
      .map(_.get)
  
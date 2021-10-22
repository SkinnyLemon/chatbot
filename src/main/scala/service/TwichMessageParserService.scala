package service

import model._

class ChannelParser:
  def parseToChannel(tags: Map[String, String], channelName: String): Channel =
    Channel(tags("room-id"), channelName)

class UserParser:
  def parseToUser(tags: Map[String, String], userName: String): User =
    User(userName, tags("display-name"), tags("user-id"))

class MessageParser:
  def parseToMessage(tags: Map[String, String], message: String) =
    val rawEmotes = tags("emotes")
    val emotes = rawEmotes.split("/").map(parseToEmote).toSeq //<emote ID>:<first index>-<last index>,<another first index>-<another last index>
    Message(emotes, message, tags("tmi-sent-ts").toLong)

  private def parseToEmote(toParse: String): Emote =
    val (emoteId, indicesRaw) = toParse.span(_ != ':') //(<emote ID>    :    <first index>-<last index>,<another first index>-<another last index>,<another first index>-<another last index>)
    val (firstIndex, lastIndex) = indicesRaw.takeWhile(_ != ',').span(_ != '-') //  <first index>-<last index>      ,    <another first index>-<another last index> => <first index> <last index>
    //val indices = indicesRaw.takeWhile(_ != ',').split("-").map(s => s.toInt) // @ Tobi, geht das hier vielleicht noch schöner? Insgesamt ohne die hässliche convertToInt alles in Funktionen reihen
    Emote(emoteId, convertToInt(firstIndex), convertToInt(lastIndex))

  private def convertToInt(indexString: String): Int= indexString.substring(1).toInt
  
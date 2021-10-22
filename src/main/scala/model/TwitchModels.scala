package model

case class TwitchMessage(channel: Channel, user: User, message: Message)

case class Emote(id: String, startIndex: Int, endIndex: Int)
case class Message(emotes: Seq[Emote], message: String, timeStamp: Long)
case class Channel(roomId: String, name: String)
case class User(userName: String, displayName: String, userId: String)

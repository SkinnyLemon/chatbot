package de.htwg.rs.chatbot
package model

case class TwitchInput(channel: Channel, user: User, message: Message)

case class Emote(id: String, startIndex: Int, endIndex: Int)

case class Message(emotes: Array[Emote], text: String, timeStamp: Long, id: String)

case class Channel(roomId: String, name: String)

case class User(userName: String, displayName: String, userId: String)

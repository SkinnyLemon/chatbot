package de.htwg.rs.chatbot.io

import java.io.{BufferedReader, BufferedWriter, InputStreamReader, OutputStreamWriter}
import java.net.Socket
import scala.util.{Failure, Try}

trait TwitchOutput {
  def sendMessage(channel: String, message: String, tags: Map[String, String] = Map.empty): Unit
}

trait RawInputProvider {
  def subscribe(subscriber: TwitchConsumer): Unit

  def unSubscribe(subscriber: TwitchConsumer): Unit
}

trait TwitchConsumer {
  def onMessage(message: String): Unit
}

trait TwitchConnection {
  def join(channel: String): Unit

  def getOutput: TwitchOutput

  def getInput: RawInputProvider

  def start(): Unit // TODO change to start for seperate Thread
}

object TwitchConnection {
  def establishConnection(accountName: String, authToken: String): TwitchConnection = new TwitchConnectionImpl(accountName, authToken)
}

class TwitchConnectionImpl(accountName: String, authToken: String) extends Thread with TwitchConnection with TwitchOutput with RawInputProvider {
  private val socket = new Socket("irc.twitch.tv", 6667)
  private val input = new BufferedReader(new InputStreamReader(socket.getInputStream))
  private val output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream))
  private var subscribers = List.empty[TwitchConsumer]

  output.write(s"PASS oauth:$authToken\r\n")
  output.write(s"NICK $accountName\r\n")
  output.write("CAP REQ :twitch.tv/tags\r\n")
  output.flush()

  override def run(): Unit = while (true) {
    val token = input.readLine
    if (token == null) {
      return
    }
    println(token)
    if (token.startsWith("PING")) {
      output.write("PONG :tmi.twitch.tv\r\n")
      output.flush()
    } else {
      subscribers.map(sub => Try(sub.onMessage(token)))
        .foreach(_.recover(_.printStackTrace()))
    }
  }

  override def join(channel: String): Unit = send(s"JOIN #${channel.toLowerCase}")

  override def sendMessage(channel: String, message: String, tags: Map[String, String] = Map.empty): Unit =
    var tagString = tags
      .map { case (key, value) => s"$key=$value" }
      .mkString(";")
    tagString =
      if (tags.isEmpty) ""
      else s"@$tagString"
    send(s"$tagString PRIVMSG #$channel :$message")

  private def send(text: String): Unit = {
    println(text)
    output.write(s"$text\r\n")
    output.flush()
  }

  override def subscribe(subscriber: TwitchConsumer): Unit = {
    subscribers = subscribers :+ subscriber
  }

  override def unSubscribe(subscriber: TwitchConsumer): Unit =
    subscribers = subscribers.filter(_ != subscriber) //TODO better way in scala to remove things from list?
    println(subscribers)

  override def getOutput: TwitchOutput = this

  override def getInput: RawInputProvider = this
}

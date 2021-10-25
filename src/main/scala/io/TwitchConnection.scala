package de.htwg.rs.chatbot
package io

import java.io.{BufferedReader, BufferedWriter, InputStreamReader, OutputStreamWriter}
import java.net.Socket
import scala.util.{Failure, Try}

trait TwitchOutput {
  def send(message: String): Unit
}

trait TwitchInput {
  def subscribe(subscriber: TwitchConsumer): Unit
}

trait TwitchConsumer {
  def onMessage(message: String): Unit
}

trait TwitchConnection {
  def join(channel: String): Unit

  def getOutput: TwitchOutput

  def getInput: TwitchInput

  def run(): Unit                             // TODO change to start for seperate Thread
}

object TwitchConnection {
  def establishConnection(accountName: String, authToken: String): TwitchConnection = new TwitchConnectionImpl(accountName, authToken)
}

private class TwitchConnectionImpl(accountName: String, authToken: String) extends Thread with TwitchConnection with TwitchOutput with TwitchInput {
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
    if (token == null)
      return
    //println(token)
    if (token.startsWith("PING")) {
      output.write("PONG :tmi.twitch.tv\r\n")
      output.flush()
    } else {
      subscribers.map(sub => Try(sub.onMessage(token)))
        .foreach(_.recover(_.printStackTrace()))
    }
  }

  override def join(channel: String): Unit = send(s"JOIN #${channel.toLowerCase}\r\n")

  override def send(message: String): Unit = {
    output.write(message)
    output.flush()
  }

  override def subscribe(subscriber: TwitchConsumer): Unit = {
    subscribers = subscribers :+ subscriber
  }

  override def getOutput: TwitchOutput = this

  override def getInput: TwitchInput = this
}

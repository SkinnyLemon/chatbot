package de.htwg.rs.chatbot
package io

import control.{CommandRegistryRegisty, `when message`}
import game.CoinFlipGameHandler
import model.TwitchInputParser

import akka.actor.ActorSystem
import akka.stream.scaladsl.{Flow, Sink, Source}
import akka.stream.{BoundedSourceQueue, Materializer}

import scala.concurrent.ExecutionContextExecutor

class AkkaAdapter(bot: BotConfig) extends TwitchConsumer :
  implicit val system: ActorSystem = ActorSystem("QuickStart")
  implicit val mat: Materializer = Materializer(system)
  implicit val ec: ExecutionContextExecutor = system.dispatcher
  val connection: TwitchConnection = TwitchConnection.establishConnection(bot.name, bot.auth)
  private var queue: BoundedSourceQueue[String] = _
  connection.getInput.subscribe(this)

  def start(sink: Sink[String, Any]): Unit =
    queue = Source.queue[String](50)
      .to(sink)
      .run()
    connection.start()

  override def onMessage(message: String): Unit =
    queue.offer(message)

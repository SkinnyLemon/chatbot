package de.htwg.rs.chatbot
package io

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.{BoundedSourceQueue, Materializer}
import akka.stream.scaladsl.{MergeHub, RunnableGraph, Sink, Source}

import java.util.concurrent.CountDownLatch
import scala.concurrent.ExecutionContextExecutor

class AkkaConnection(bot: BotConfig, sink: Sink[String, Any]) extends TwitchConsumer {
  implicit val system: ActorSystem = ActorSystem("QuickStart")
  implicit val mat: Materializer = Materializer(system)
  implicit val ec: ExecutionContextExecutor = system.dispatcher
  private val queue: BoundedSourceQueue[String] =
    Source.queue[String](50)
      .to(sink)
      .run()
  val connection: TwitchConnection = TwitchConnection.establishConnection(bot.name, bot.auth)
  connection.getInput.subscribe(this)


  def start(): Unit = connection.start()

  override def onMessage(message: String): Unit =
    queue.offer(message)
}

object AkkaTest extends App {
  val bot = Config.bots.head
  val connection = new AkkaConnection(bot, Sink.foreach(println))
  connection.connection.join("ESL_CSGO")
  connection.start()
}

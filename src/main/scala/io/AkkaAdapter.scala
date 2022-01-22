package de.htwg.rs.chatbot
package io

import akka.actor.ActorSystem
import akka.stream.scaladsl.{Flow, Sink, Source}
import akka.stream.{BoundedSourceQueue, Materializer}
import de.htwg.rs.chatbot.control.{CommandRegistryRegisty, `when message`}
import de.htwg.rs.chatbot.game.CoinFlipGameHandler
import de.htwg.rs.chatbot.model.TwitchInputParser

import scala.concurrent.ExecutionContextExecutor

class AkkaAdapter(bot: BotConfig) extends TwitchConsumer {
  implicit val system: ActorSystem = ActorSystem("QuickStart")
  implicit val mat: Materializer = Materializer(system)
  implicit val ec: ExecutionContextExecutor = system.dispatcher
  private var queue: BoundedSourceQueue[String] = _
  val connection: TwitchConnection = TwitchConnection.establishConnection(bot.name, bot.auth)
  connection.getInput.subscribe(this)

  def start(sink: Sink[String, Any]): Unit = {
    queue = Source.queue[String](50)
        .to(sink)
        .run()
    connection.start()
  }

  override def onMessage(message: String): Unit =
    queue.offer(message)
}

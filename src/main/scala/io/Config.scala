package de.htwg.rs.chatbot
package io

import java.nio.file.{Files, Paths}
import scala.jdk.CollectionConverters.IteratorHasAsScala

object Config:
  private val botFile = Paths.get("config/bots")
  val bots: List[BotConfig] = Files.lines(botFile)
    .map(_.split(";"))
    .map(c => BotConfig(c(0), c(1)))
    .iterator().asScala
    .toList

case class BotConfig(name: String, auth: String)

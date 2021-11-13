package de.htwg.rs.chatbot
package model

import control.ChannelOutput

trait Command {
  def handle(message: TwitchInput): (Command, Option[String])
}

trait Trigger {
  def isTriggered(message: TwitchInput): Boolean
}

case class PrefixTrigger(prefix: String) extends Trigger {
  override def isTriggered(message: TwitchInput): Boolean = message.message.text.startsWith(prefix)
}

case class SuffixTrigger(suffix: String) extends Trigger {
  override def isTriggered(message: TwitchInput): Boolean = message.message.text.endsWith(suffix)
}

case class ContainsTrigger(toContain: String) extends Trigger {
  override def isTriggered(message: TwitchInput): Boolean = message.message.text.contains(toContain)
}

case class EqualsTrigger(toEqual: String) extends Trigger {
  override def isTriggered(message: TwitchInput): Boolean = message.message.text.equals(toEqual)
}

trait ResponseGenerator {
  def generateResponse(message: TwitchInput): String
}

case class BasicResponseGenerator(response: String) extends ResponseGenerator {
  override def generateResponse(message: TwitchInput): String = response
}

case class DefaultCommand(trigger: Trigger, responseGenerator: ResponseGenerator) extends Command {
  override def handle(message: TwitchInput): (Command, Option[String]) =
    (this,
      if (trigger.isTriggered(message))
        Some(responseGenerator.generateResponse(message))
      else
        None
    )
}

package de.htwg.rs.chatbot
package control

import model.{BasicResponseGenerator, Command, ContainsTrigger, DefaultCommand, EqualsTrigger, PrefixTrigger, SuffixTrigger, Trigger}

import de.htwg.rs.chatbot.io.TwitchOutput

object `when message` {
  def `starts with`(prefix: String): TriggerDefined = TriggerDefined(PrefixTrigger(prefix))
  def contains(toContain: String): TriggerDefined = TriggerDefined(ContainsTrigger(toContain))
  def is(toEqual: String): TriggerDefined = TriggerDefined(EqualsTrigger(toEqual))
  def `ends with`(suffix: String): TriggerDefined = TriggerDefined(SuffixTrigger(suffix))
}

case class TriggerDefined(trigger: Trigger) {
  def `respond with`(reply: String): Command = DefaultCommand(trigger, BasicResponseGenerator(reply))
}
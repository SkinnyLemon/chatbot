package de.htwg.rs.chatbot
package control

import model.*

object `when message`:
  def `starts with`(prefix: String): TriggerDefined = TriggerDefined(PrefixTrigger(prefix))

  def contains(toContain: String): TriggerDefined = TriggerDefined(ContainsTrigger(toContain))

  def is(toEqual: String): TriggerDefined = TriggerDefined(EqualsTrigger(toEqual))

  def `ends with`(suffix: String): TriggerDefined = TriggerDefined(SuffixTrigger(suffix))

case class TriggerDefined(trigger: Trigger):
  def `respond with`(reply: String): Command = DefaultCommand(trigger, BasicResponseGenerator(reply))
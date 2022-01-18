package de.htwg.rs.chatbot
package iris

import de.htwg.rs.chatbot.iris.{Evaluator}
import model.{Command, TwitchInput}
import de.htwg.rs.chatbot.model.Iris

case class IrisHandler(irisEvaluator: Evaluator) extends Command {
  override def handle(input: TwitchInput): (Command, Option[String]) = {
    val command = input.message.text
    if (command.startsWith("predict iris")) then executePrediction(command) else (this, None)
  }

  def executePrediction(command: String): (Command, Option[String]) = {
    //  predict iris (6.5,2.8,5.6,2.1)
    val irisPattern = "[0-9]+\\.[0-9]+".r
    val matches = irisPattern.findAllIn(command).toArray
    //val numbers = matches.foreach(e => e.toDouble)

    if (matches.length != 4) then
      ((this, Some("Invalid format. Do like this: predict iris 6.5,2.8,5.6,2.1 ")))
    else
      val resultIris = irisEvaluator.evaluate(Iris(matches(0).toDouble, matches(1).toDouble, matches(2).toDouble, matches(3).toDouble))
      (this, Some("I predict, that your Iris is of kind: " + resultIris.irisClass))
  }
}
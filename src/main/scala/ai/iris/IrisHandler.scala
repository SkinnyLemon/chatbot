package de.htwg.rs.chatbot.ai.iris

import de.htwg.rs.chatbot.ai.{Classifier, Evaluator}
import de.htwg.rs.chatbot.model.{Command, Iris, TwitchInput}

case class IrisHandler(irisEvaluator: Evaluator[Iris]) extends Command :
  override def handle(input: TwitchInput): (Command, Option[String]) =
    val command = input.message.text
    if (command.startsWith("predict iris")) executePrediction(command) else (this, None)

  def executePrediction(command: String): (Command, Option[String]) =
    //  predict iris (6.5,2.8,5.6,2.1)
    val irisPattern = "[0-9]+\\.[0-9]+".r
    val matches = irisPattern.findAllIn(command).toArray
    if (matches.length != 4)
      ((this, Some("Invalid format. Do like this: predict iris 6.5,2.8,5.6,2.1 ")))
    else
      val resultIris = irisEvaluator.evaluate(Iris(matches(0).toDouble, matches(1).toDouble, matches(2).toDouble, matches(3).toDouble))
      (this, Some("I predict, that your Iris is of kind: " + resultIris.irisClass))
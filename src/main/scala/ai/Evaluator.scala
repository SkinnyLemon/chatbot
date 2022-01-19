package de.htwg.rs.chatbot
package ai

trait Evaluator[T] {
  def evaluate(toEvaluate: T): T
}

trait Classifier[T] {
  def initialize(): Evaluator[T] 
}
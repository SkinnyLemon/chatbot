package de.htwg.rs.chatbot
package model

case class Iris(sepalLength: Double, sepalWidth: Double, petalLength: Double, petalWidth: Double, irisClass: String = ""):
  override def toString: String =
    String.format(s"Iris Class = $irisClass, Data[ Sepal Length = $sepalLength, Sepal Width = $sepalWidth, Petal Length = $petalLength, Petal Width = $petalWidth ]")

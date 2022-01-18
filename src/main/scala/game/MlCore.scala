package de.htwg.rs.chatbot
package game

import model.{TwitchInput, User}

object MLCore {
  def startGame(lowest: Int, highest: Int): Running =
    val (firstCard, cardPile) = Cards(lowest to highest).drawCard()
    Running(cardPile, List(firstCard))
}

sealed abstract class MLCore(discardPile: List[Int]) {
  def current: Int = discardPile.last
}

case class Running(cards: Cards, discardPile: List[Int], streak: Int = 0) extends MLCore(discardPile) {
  def betMore = bet(_ > current)
  def betLess = bet(_ < current)

  private def bet(betWon: Int=> Boolean): MLCore =
    val (pulled, newPile) = cards.drawCard()
    if betWon(pulled) then
      if newPile.isEmpty then
        Win(discardPile :+ pulled, streak + 1)
      else
        Running(newPile, discardPile :+ pulled, streak + 1)
    else
      Loss(discardPile :+ pulled, streak)
}

case class Win(previous: List[Int], streak: Int) extends MLCore(previous)
case class Loss(previous: List[Int], streak: Int) extends MLCore(previous)

private case class Cards (private val pile: IndexedSeq[Int]) {
  def isEmpty: Boolean = pile.isEmpty

  def drawCard(): (Int, Cards) =
    val rnd = new scala.util.Random
    val cardIndex = rnd.nextInt(pile.length)
    val cardValue = pile(cardIndex)
    val newPile = pile.filter(_ != cardValue)
    (cardValue, copy(newPile))
}




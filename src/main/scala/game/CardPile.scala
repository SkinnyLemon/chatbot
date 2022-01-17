package de.htwg.rs.chatbot
package game

case class CardPile(pile: List[Int]) {

  def drawCard(): (Int, CardPile) =
    val rnd = new scala.util.Random
    val cardIndex = rnd.nextInt(pile.length)
    val cardValue = pile(cardIndex)
    val newPile = pile.filter(_ != cardValue)
    (cardValue, copy(newPile))

  def hasOneCardLeft(): Boolean =
    return pile.length == 1
}
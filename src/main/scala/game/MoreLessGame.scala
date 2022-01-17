package de.htwg.rs.chatbot
package game

import model.TwitchInput

case class MoreLessGame(cardPile: CardPile = new CardPile((7 to 12).toList), lastCard: Int = 0) {

  private def processInput(input: TwitchInput): (Option[MoreLessGame], Option[String]) =
    input.message.text.toLowerCase() match
      case "s" | "start" => startOffNewGame()
      case "m" | "h" => processGame(true)
      case "l" | "t" => processGame(false)


  private def startOffNewGame(): (Option[MoreLessGame], Option[String]) =
    val result = cardPile.drawCard()
    val matchResultMessage = generateMatchResultMessage(result._1, None)
    (Some(nextRound(result._2, result._1)), Some(matchResultMessage))

  private def nextRound(pile: CardPile, drawnCard: Int): MoreLessGame =
    copy(pile, drawnCard)


  private def processGame(higherChoice: Boolean): (Option[MoreLessGame], Option[String]) =
    val result = cardPile.drawCard()
    val wasLoss = isLoss(higherChoice, result._1)
    val matchResultMessage = generateMatchResultMessage(result._1, Some(wasLoss))
    (Some(nextRound(result._2, result._1)), Some(matchResultMessage))

  private def isLoss(higherChoice: Boolean, drawnCard: Int): Boolean =
    if higherChoice then drawnCard < lastCard else drawnCard > lastCard

  private def generateMatchResultMessage(drawnCard: Int, isLoss: Option[Boolean]): String =
    isLoss match {
      case Some(true) => s"O neim! du hast verloren!!! Die letzte Karte war $lastCard und es wurde eine $drawnCard gezogen.  lelelelel PogChamp PogChamp PogChamp"
      case Some(false) => s"Gut gemacht! Es wurde eine $drawnCard gezogen. Das Spiel geht weiter!"
      case None => s"Es geht los! Es wurde eine $drawnCard gezogen. Was tutst du? Höher? Tiefer? (h/t)  (m/l)"
    }


}

package de.htwg.rs.chatbot
package game

import model.{TwitchInput, User}

case class MoreLessGame(player: User, cardPile: CardPile = new CardPile((7 to 12).toList), lastCard: Int = 0) {
  def handle(input: TwitchInput): (Option[MoreLessGame], Option[String]) =
    if (input.user.userId == player.userId)
      processInput(input)
    else
      (Some(this), None)


  private def processInput(input: TwitchInput): (Option[MoreLessGame], Option[String]) =
    input.message.text.toLowerCase() match
      case "s" | "start" => startOffNewGame()
      case "m" | "h" => processGame(true)
      case "l" | "t" => processGame(false)
      case "e" | "exit" => endGame()
      case _ => (Some(this), None)

  private def startOffNewGame(): (Option[MoreLessGame], Option[String]) =
    val result = cardPile.drawCard()
    val matchResultMessage = generateMatchResultMessage(result._1, None)
    (Some(nextRound(result._2, result._1)), Some(matchResultMessage))

  private def nextRound(pile: CardPile, drawnCard: Int): MoreLessGame =
    copy(player, pile, drawnCard)


  private def processGame(higherChoice: Boolean): (Option[MoreLessGame], Option[String]) =
    val result = cardPile.drawCard()
    val wasLoss = isLoss(higherChoice, result._1)
    val matchResultMessage = generateMatchResultMessage(result._1, Some(wasLoss))
    if !wasLoss && !cardPile.hasOneCardLeft() then
      (Some(nextRound(result._2, result._1)), Some(matchResultMessage))
    else
      (None, Some(matchResultMessage))

  private def isLoss(higherChoice: Boolean, drawnCard: Int): Boolean =
    if higherChoice then drawnCard < lastCard else drawnCard > lastCard

  private def generateMatchResultMessage(drawnCard: Int, isLoss: Option[Boolean]): String =
    isLoss match {
      case Some(true) => s"O neim! du hast verloren!!! Die letzte Karte war $lastCard und es wurde eine $drawnCard gezogen.  lelelelel PogChamp PogChamp PogChamp"
      case Some(false) => if cardPile.hasOneCardLeft() then  s"Spiel vorbei! Du hast gewonnen! PogChamp PogChamp PogChamp" else s"Gut gemacht! Es wurde eine $drawnCard gezogen. Das Spiel geht weiter!"
      case None => s"Es geht los! Es wurde eine $drawnCard gezogen. Was tutst du? Höher? Tiefer? (h/t)  (m/l)"
    }

  private def endGame(): (Option[MoreLessGame], Option[String]) =
    (None, Some("ok baiii"))
}

package de.htwg.rs.chatbot
package actor

object Messages {
  case class Deposit(amount: Double) {
    require(amount > 0)
  }

  case class Withdraw(amount: Double) {
    require(amount > 0)
  }

  case class CreateBankAccount(name: String)

  case class Transaction(amount: Int, from: String, to: String)

  case object GetBalance

  case object PrintBalance

  case object Done

  case object Failed

}

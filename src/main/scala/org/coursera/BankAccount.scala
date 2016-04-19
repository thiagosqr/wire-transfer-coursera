package org.coursera

import akka.actor.Actor
import akka.event.LoggingReceive
import org.coursera.BankAccount._

/**
  * Created by thiago on 4/12/16.
  */
object BankAccount {

  case class Deposit(amount: BigInt){
    require(amount > 0)
  }

  case class Withdraw(amount: BigInt){
    require(amount > 0)
  }

  case object Done
  case object Failed

}

class BankAccount extends Actor {

  var balance = BigInt(0)

  override def receive: Receive = LoggingReceive {

    case Deposit(amount) =>
      balance += amount
      sender ! Done
    case Withdraw(amount) if amount <= balance =>
      balance -= amount
      sender ! Done
    case _ => sender ! Failed

  }
}

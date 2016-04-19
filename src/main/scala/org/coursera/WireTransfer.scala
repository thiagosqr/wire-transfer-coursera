package org.coursera

import akka.actor.{Actor, ActorRef}
import akka.actor.Actor.Receive
import akka.event.LoggingReceive
import org.coursera.WireTransfer.{Done, Failed, Transfer}

/**
  * Created by thiago on 4/12/16.
  */
object WireTransfer {
  case class Transfer(from: ActorRef, to: ActorRef, amount: BigInt)
  case object Done
  case object Failed

}

class WireTransfer extends Actor {

  override def receive: Receive = LoggingReceive {
    case Transfer(from, to, amount) =>
      from ! BankAccount.Withdraw(amount)
      context.become(awaitFrom(to,amount,sender))
  }

  def awaitFrom(to: ActorRef, amount: BigInt, costumer: ActorRef): Receive = LoggingReceive {
    case BankAccount.Done =>
      to ! BankAccount.Deposit(amount)
      context.become(awaitTo(costumer))
    case BankAccount.Failed =>
      costumer ! Failed
      context.stop(self)
  }

  def awaitTo(costumer: ActorRef): Receive = LoggingReceive {
    case BankAccount.Done =>
      costumer ! Done
      context.stop(self)
  }

}

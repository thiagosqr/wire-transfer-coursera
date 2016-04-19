package org.coursera

import akka.actor.Actor.Receive
import akka.actor.{Actor, Props}
import akka.event.LoggingReceive

/**
  * Created by thiago on 4/12/16.
  */
class TransferMain extends Actor{

  val accountA = context.actorOf(Props[BankAccount], "accountA")
  val accountB = context.actorOf(Props[BankAccount], "accountB")

  accountA ! BankAccount.Deposit(100)

  override def receive: Receive = LoggingReceive {
    case BankAccount.Done => transfer(50)
  }

  def transfer(amount: Int): Unit = {
    val transaction = context.actorOf(Props[WireTransfer],"transfer")
    transaction ! WireTransfer.Transfer(accountA,accountB,amount)

    context.become(LoggingReceive{
      case WireTransfer.Done =>
        println("Finished")
        context.stop(self)
    })

  }
}

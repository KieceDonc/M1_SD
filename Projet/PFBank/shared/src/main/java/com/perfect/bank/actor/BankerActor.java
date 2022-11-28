package com.perfect.bank.actor;

import akka.actor.ActorSelection;
import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;

import com.perfect.bank.messages.Messages;
import com.perfect.bank.messages.Messages.Deposit;
import com.perfect.bank.messages.Messages.GetBalance;
import com.perfect.bank.messages.Messages.Withdraw;

import akka.event.LoggingAdapter;
import akka.event.Logging;

public class BankerActor extends AbstractActor {

  private LoggingAdapter log = Logging.getLogger(this.getContext().system(), this);

  private ActorSelection bankActor;

  private int UID = -1;

  public BankerActor(ActorSelection bankActor) {
    this.bankActor = bankActor;
    this.declareToBank();
  }

  @Override
  public Receive createReceive() {
    return receiveBuilder()
        .match(Deposit.class, message -> clientDeposit(getSender(), message))
        .match(Withdraw.class, message -> clientWithdraw(getSender(), message))
        .match(GetBalance.class, message -> clientGetBalance(getSender(), message))
        .build();
  }

  public void declareToBank() {
    bankActor.tell(new Messages.DeclareAsBanker(), getSelf());
  }

  public void clientDeposit(ActorRef actor, Deposit depositMsg) {
    this.log.info("Deposit succeed");
  }

  public void clientWithdraw(ActorRef actor, Withdraw withdrawMsg) {
    this.log.info("Widthdraw succeed");
  }

  public void clientGetBalance(ActorRef actor, GetBalance getBalanceMsg) {
    actor.tell(500.0, getSelf());
  }

  public static Props props(ActorSelection bankActor) {
    return Props.create(BankerActor.class, bankActor);
  }
}
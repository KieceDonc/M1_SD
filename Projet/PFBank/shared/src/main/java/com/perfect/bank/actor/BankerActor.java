package com.perfect.bank.actor;

import akka.actor.ActorSelection;
import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;

import java.sql.SQLException;

import com.perfect.bank.classes.PLSQLInterface;
import com.perfect.bank.exceptions.NoAccountException;
import com.perfect.bank.exceptions.NotEnoughMoneyException;
import com.perfect.bank.messages.Messages;
import com.perfect.bank.messages.Messages.Deposit;
import com.perfect.bank.messages.Messages.GetBalance;
import com.perfect.bank.messages.Messages.Withdraw;
import com.perfect.bank.messages.Messages.Shutdown;

import akka.event.LoggingAdapter;
import akka.event.Logging;

public class BankerActor extends AbstractActor {

  private LoggingAdapter log = Logging.getLogger(this.getContext().system(), this);

  private ActorSelection bankActor;

  private PLSQLInterface plsqlInterface;

  public BankerActor(ActorSelection bankActor) {
    this.bankActor = bankActor;
    this.declareToBank();

    try {
      plsqlInterface = new PLSQLInterface();
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  @Override
  public Receive createReceive() {
    return receiveBuilder()
        .match(Deposit.class, message -> clientDeposit(getSender(), message))
        .match(Withdraw.class, message -> clientWithdraw(getSender(), message))
        .match(GetBalance.class, message -> clientGetBalance(getSender(), message))
        .match(Shutdown.class, message -> shtudown())
        .build();
  }

  public void declareToBank() {
    bankActor.tell(new Messages.DeclareAsBanker(), getSelf());
  }

  public void clientDeposit(ActorRef actor, Deposit depositMsg) {
    try {
      this.plsqlInterface.deposit(depositMsg.getClientUID(), depositMsg.getAmount());
    } catch (SQLException e) {
      e.printStackTrace();
    } catch (NoAccountException e) {
      e.printStackTrace();
    }
  }

  public void clientWithdraw(ActorRef actor, Withdraw withdrawMsg) {
    try {
      this.plsqlInterface.withdraw(withdrawMsg.getClientUID(), withdrawMsg.getAmount());
    } catch (SQLException e) {
      e.printStackTrace();
    } catch (NoAccountException e) {
      e.printStackTrace();
    } catch (NotEnoughMoneyException e) {
      e.printStackTrace();
    }
  }

  public void clientGetBalance(ActorRef actor, GetBalance getBalanceMsg) {
    try {
      double balance = this.plsqlInterface.getBalance(getBalanceMsg.getClientUID());
      actor.tell(balance, getSelf());
    } catch (SQLException e) {
      e.printStackTrace();
      actor.tell(-1, getSelf());
    } catch (NoAccountException e) {
      e.printStackTrace();
      actor.tell(-1, getSelf());
    }
  }

  private void shtudown() {
    try {
      plsqlInterface.closeDBConnection();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static Props props(ActorSelection bankActor) {
    return Props.create(BankerActor.class, bankActor);
  }
}
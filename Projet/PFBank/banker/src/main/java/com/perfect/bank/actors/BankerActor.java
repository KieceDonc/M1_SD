package com.perfect.bank.actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.Props;

import java.sql.SQLException;

import com.perfect.bank.exceptions.NoAccountException;
import com.perfect.bank.exceptions.NotEnoughMoneyException;
import com.perfect.bank.helpers.SGBDInterface;
import com.perfect.bank.messages.Messages;
import com.perfect.bank.messages.Messages.Deposit;
import com.perfect.bank.messages.Messages.GetBalance;
import com.perfect.bank.messages.Messages.CreateClientUID;
import com.perfect.bank.messages.Messages.IsClientExist;
import com.perfect.bank.messages.Messages.Withdraw;
import com.perfect.bank.messages.Messages.Shutdown;

import akka.event.LoggingAdapter;
import akka.event.Logging;

public class BankerActor extends AbstractActor {

  private LoggingAdapter log = Logging.getLogger(this.getContext().system(), this);

  private ActorSelection bankActor;

  private SGBDInterface sgbdInterface;

  public BankerActor(ActorSelection bankActor) {
    this.bankActor = bankActor;
    this.declareToBank();

    try {
      sgbdInterface = new SGBDInterface();
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  @Override
  public Receive createReceive() {
    return receiveBuilder()
        .match(IsClientExist.class, message -> isClientExist(getSender(), message.getClientUID()))
        .match(CreateClientUID.class, message -> createClientUID(getSender()))
        .match(Deposit.class, message -> clientDeposit(getSender(), message))
        .match(Withdraw.class, message -> clientWithdraw(getSender(), message))
        .match(GetBalance.class, message -> clientGetBalance(getSender(), message))
        .match(Shutdown.class, message -> shutdown())
        .build();
  }

  private void declareToBank() {
    bankActor.tell(new Messages.DeclareAsBanker(), getSelf());
  }

  public void isClientExist(ActorRef actor, int clientUID) {
    boolean exist = false;

    try {
      exist = this.sgbdInterface.isClientInDB(clientUID);
    } catch (SQLException e) {
      e.printStackTrace();
    }

    actor.tell(exist, this.getSelf());
  }

  public void createClientUID(ActorRef actor) {
    int UID = -1;
    try {
      UID = this.sgbdInterface.createClient();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    actor.tell(UID, this.getSelf());
  }

  public void clientDeposit(ActorRef actor, Deposit depositMsg) {
    try {
      this.sgbdInterface.deposit(depositMsg.getClientUID(), depositMsg.getAmount());
    } catch (SQLException e) {
      e.printStackTrace();
    } catch (NoAccountException e) {
      e.printStackTrace();
    }
  }

  public void clientWithdraw(ActorRef actor, Withdraw withdrawMsg) {
    try {
      this.sgbdInterface.withdraw(withdrawMsg.getClientUID(), withdrawMsg.getAmount());
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
      double balance = this.sgbdInterface.getBalance(getBalanceMsg.getClientUID());
      actor.tell(new Messages.GetBalanceResponse(balance), this.getSelf());
    } catch (SQLException e) {
      e.printStackTrace();
      actor.tell(-1, this.getSelf());
    } catch (NoAccountException e) {
      e.printStackTrace();
      actor.tell(-1, this.getSelf());
    }
  }

  private void shutdown() {
    try {
      sgbdInterface.closeDBConnection();
      bankActor.tell(new Messages.UnvalidateBanker(), this.getSelf());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static Props props(ActorSelection bankActor) {
    return Props.create(BankerActor.class, bankActor);
  }
}
package com.perfect.bank.actors;

import akka.actor.ActorSelection;
import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;

import com.perfect.bank.messages.Messages;
import com.perfect.bank.messages.Messages.Deposit;
import com.perfect.bank.messages.Messages.GetBalance;
import com.perfect.bank.messages.Messages.GetBalanceResponse;
import com.perfect.bank.messages.Messages.SetClientUID;
import com.perfect.bank.messages.Messages.Withdraw;

import akka.event.LoggingAdapter;
import akka.event.Logging;

public class ClientActor extends AbstractActor {

    private LoggingAdapter log = Logging.getLogger(this.getContext().system(), this);

    private ActorSelection bankActor;
    private ActorRef respondTo;

    private int UID = -1;

    public ClientActor(ActorSelection bankActor, int clientUID) {
        this.bankActor = bankActor;
        this.UID = clientUID;
    }

    public ClientActor(ActorSelection bankActor) {
        this.bankActor = bankActor;
        bankActor.tell(new Messages.CreateClientUID(), getSelf());
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Deposit.class, message -> deposit(message.getAmount()))
                .match(Withdraw.class, message -> withdraw(message.getAmount()))
                .match(GetBalance.class, message -> getBalance(getSender()))
                .match(SetClientUID.class, message -> setUID(message.getClientUID()))
                .match(GetBalanceResponse.class, message -> getBalanceResponse(message.getBalance()))
                .build();
    }

    private void setUID(int UID) {
        this.UID = UID;
    }

    public int getUID() {
        if (this.UID < 0) {
            System.out.println("Erreur dans l'initialisation du client");
            System.exit(0);
        }
        return this.UID;
    }

    public void deposit(double amount) {
        bankActor.tell(new Messages.Deposit(this.getUID(), amount), this.getSelf());
    }

    public void withdraw(double amount) {
        bankActor.tell(new Messages.Withdraw(this.getUID(), amount), this.getSelf());
    }

    public void getBalance(ActorRef respondTo) {
        this.respondTo = respondTo;
        this.bankActor.tell(new Messages.GetBalance(this.getUID()), this.getSelf());
    }

    public void getBalanceResponse(double balance) {
        respondTo.tell(balance, this.getSelf());
    }

    public static Props props(ActorSelection bankActor) {
        return Props.create(ClientActor.class, bankActor);
    }

    public static Props props(ActorSelection bankActor, int clientUID) {
        return Props.create(ClientActor.class, bankActor, clientUID);
    }
}
package com.perfect.bank.actor;

import akka.actor.ActorSelection;
import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.pattern.Patterns;
import java.time.Duration;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

import com.perfect.bank.messages.Messages;
import com.perfect.bank.messages.Messages.Deposit;
import com.perfect.bank.messages.Messages.GetBalance;
import com.perfect.bank.messages.Messages.Withdraw;

import akka.event.LoggingAdapter;
import akka.event.Logging;

public class ClientActor extends AbstractActor {

    private LoggingAdapter log = Logging.getLogger(this.getContext().system(), this);

    private ActorSelection bankActor;

    private int UID = -1;

    public ClientActor(ActorSelection bankActor, int clientUID) {
        this.bankActor = bankActor;
        this.UID = clientUID;
    }

    public ClientActor(ActorSelection bankActor) {
        this.bankActor = bankActor;
        this.getUID();
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Deposit.class, message -> deposit(message.getAmount()))
                .match(Withdraw.class, message -> withdraw(message.getAmount()))
                .match(GetBalance.class, message -> getBalance())
                .build();
    }

    public int getUID() {
        try {
            if (this.UID < 0) {
                CompletionStage<Object> result = Patterns.ask(bankActor,
                        new Messages.GetClientUID(),
                        Duration.ofSeconds(10));

                this.UID = (int) result.toCompletableFuture().get();

                System.out.println("UID reçu : " + this.UID);
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return this.UID;
    }

    public void deposit(double amount) {
        bankActor.tell(new Messages.Deposit(this.getUID(), amount), getSelf());
    }

    public void withdraw(double amount) {
        bankActor.tell(new Messages.Withdraw(this.getUID(), amount), getSelf());
    }

    public double getBalance() {
        try {
            CompletionStage<Object> result = Patterns.ask(bankActor,
                    new Messages.GetBalance(this.getUID()),
                    Duration.ofSeconds(10));

            return (double) result.toCompletableFuture().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return 0.0d;
    }

    public static Props props(ActorSelection bankActor) {
        return Props.create(ClientActor.class, bankActor);
    }

    public static Props props(ActorSelection bankActor, int clientUID) {
        return Props.create(ClientActor.class, bankActor, clientUID);
    }
}
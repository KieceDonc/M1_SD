package com.perfect.bank.actor;

import akka.actor.ActorSelection;
import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.pattern.Patterns;
import java.time.Duration;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

import com.perfect.bank.messages.Messages;

import akka.event.LoggingAdapter;
import akka.event.Logging;

public class ClientActor extends AbstractActor {

    private LoggingAdapter log = Logging.getLogger(this.getContext().system(), this);

    private ActorSelection bankActor;

    private int UID = -1;

    public ClientActor(ActorSelection bankActor) {
        this.bankActor = bankActor;
        this.getUID();

        for (int i = 0; i < 100; i++) {
            try {
                Thread.sleep(1000);
                switch (i % 3) {
                    case 0: {
                        deposit(5.0);
                        break;
                    }
                    case 1: {
                        withdraw(5.0);
                        break;
                    }
                    case 2: {
                        this.log.info("Balance : " + getBalance());
                        break;
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

        }
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder().build();
    }

    public int getUID() {
        try {
            if (this.UID < 0) {
                CompletionStage<Object> result = Patterns.ask(bankActor,
                        new Messages.GetClientUID(),
                        Duration.ofSeconds(10));

                this.UID = (int) result.toCompletableFuture().get();

                this.log.info("UID reçu : " + this.UID);
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

    public int getBalance() {
        try {
            CompletionStage<Object> result = Patterns.ask(bankActor,
                    new Messages.GetBalance(this.getUID()),
                    Duration.ofSeconds(10));

            return (int) result.toCompletableFuture().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static Props props(ActorSelection bankActor) {
        return Props.create(ClientActor.class, bankActor);
    }
}
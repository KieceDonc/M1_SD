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
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder().build();
    }

    public int getUID() {
        try {
            if (this.UID < 0) {
                CompletionStage<Object> result = Patterns.ask(bankActor,
                        new Messages.GetUID(),
                        Duration.ofSeconds(10));

                this.UID = (int) result.toCompletableFuture().get();

                System.out.println("[Client] UID reçu : " + this.UID);
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return this.UID;
    }

    public static Props props(ActorSelection bankActor) {
        return Props.create(ClientActor.class, bankActor);
    }
}
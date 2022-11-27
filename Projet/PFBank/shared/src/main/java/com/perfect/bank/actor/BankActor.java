package com.perfect.bank.actor;

import akka.actor.ActorRef;

import akka.actor.AbstractActor;
import akka.actor.Props;

import akka.event.LoggingAdapter;
import akka.event.Logging;

import com.perfect.bank.messages.Messages.GetUID;

public class BankActor extends AbstractActor {

    private LoggingAdapter log = Logging.getLogger(this.getContext().system(), this);

    private int clientLastUID = 0;

    public BankActor() {
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder().match(GetUID.class, message -> getUID(getSender())).build();
    }

    public synchronized void getUID(ActorRef actor) {
        actor.tell(clientLastUID++, this.getSelf());
    }

    public static Props props() {
        return Props.create(BankActor.class);
    }
}

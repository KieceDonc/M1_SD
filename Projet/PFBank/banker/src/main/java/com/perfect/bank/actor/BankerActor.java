package com.perfect.bank.actor;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.pattern.Patterns;
import java.time.Duration;
import java.util.concurrent.CompletionStage;
import akka.event.LoggingAdapter;
import akka.event.Logging;

public class BankerActor extends AbstractActor {

    private LoggingAdapter log = Logging.getLogger(this.getContext().system(), this);

    public BankerActor() {

    }

    @Override
    public Receive createReceive() {
        return receiveBuilder().build();
    }

    public static Props props() {
        return Props.create(BankerActor.class);
    }

}

package com.perfect.bank.actor;

import java.util.ArrayList;
import java.util.HashMap;

import com.perfect.bank.messages.Messages.GetBankerUID;
import com.perfect.bank.messages.Messages.GetClientUID;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class BankActor extends AbstractActor {

    private LoggingAdapter log = Logging.getLogger(this.getContext().system(), this);

    // Describe which banker a client has
    // Key : ClientUID
    // Value : BankerUID
    private HashMap<Integer, Integer> relations = new HashMap<>();

    // List of all bankersUID
    private ArrayList<Integer> bankers = new ArrayList<>();

    private int lastUID = 0;

    public BankActor() {
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(GetBankerUID.class, message -> getBankerUID(getSender()))
                .match(GetClientUID.class, message -> getClientUID(getSender()))
                .build();
    }

    public void getBankerUID(ActorRef actor) {
        Integer bankerUID = this.generateUID();
        actor.tell(bankerUID, this.getSelf());
        bankers.add(bankerUID);

        this.log.info("New banker, UID : " + bankerUID);
    }

    public void getClientUID(ActorRef actor) {
        Integer clientUID = this.generateUID();
        actor.tell(clientUID, this.getSelf());

        this.log.info("New client, UID : " + clientUID);

        int bankerUID = getBankerUIDForClient();
        relations.put(clientUID, bankerUID);

        this.log.info("New relation, clientUID : " + clientUID + ", bankerUID : " + bankerUID);
    }

    public int getBankerUIDForClient() {
        int bankersNumber = bankers.size();
        int indexOfBanker = (int) Math.random() * bankersNumber;
        return this.bankers.get(indexOfBanker);
    }

    public synchronized int generateUID() {
        this.log.info("New UID given : " + this.lastUID);
        return this.lastUID++;
    }

    public static Props props() {
        return Props.create(BankActor.class);
    }
}

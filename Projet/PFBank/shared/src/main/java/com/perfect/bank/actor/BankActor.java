package com.perfect.bank.actor;

import java.util.ArrayList;

import com.perfect.bank.messages.Messages.DeclareAsBanker;
import com.perfect.bank.messages.Messages.Deposit;
import com.perfect.bank.messages.Messages.GetBalance;
import com.perfect.bank.messages.Messages.GetClientUID;
import com.perfect.bank.messages.Messages.Withdraw;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class BankActor extends AbstractActor {

    private LoggingAdapter log = Logging.getLogger(this.getContext().system(), this);

    // List of all bankers
    private ArrayList<ActorRef> bankers = new ArrayList<>();
    private int indexOfLastBanker = 0;

    private int lastUID = 0;

    public BankActor() {
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(GetClientUID.class, message -> getClientUID(getSender()))
                .match(DeclareAsBanker.class, message -> registerBanker(getSender()))
                .match(Deposit.class, message -> clientDeposit(getSender(), message))
                .match(Withdraw.class, message -> clientWithdraw(getSender(), message))
                .match(GetBalance.class, message -> clientGetBalance(getSender(), message))
                .build();
    }

    public void getClientUID(ActorRef actor) {
        Integer clientUID = this.generateUID();
        actor.tell(clientUID, this.getSelf());

        this.log.info("New client, UID : " + clientUID);
    }

    public void registerBanker(ActorRef banker) {
        // Banker isn't register yet
        if (bankers.lastIndexOf(banker) == -1) {
            bankers.add(banker);
            this.log.info("New banker register");
        }

    }

    public synchronized int generateUID() {
        this.log.info("New UID given : " + this.lastUID);
        return this.lastUID++;
    }

    public void clientDeposit(ActorRef actor, Deposit depositMsg) {
        this.getABanker().forward(depositMsg, getContext());
    }

    public void clientWithdraw(ActorRef actor, Withdraw withdrawMsg) {
        this.getABanker().forward(withdrawMsg, getContext());
    }

    public void clientGetBalance(ActorRef actor, GetBalance getBalanceMsg) {
        this.getABanker().forward(getBalanceMsg, getContext());
    }

    public synchronized ActorRef getABanker() {
        if (indexOfLastBanker >= bankers.size()) {
            indexOfLastBanker = 0;
        }
        return bankers.get(indexOfLastBanker++);
    }

    public static Props props() {
        return Props.create(BankActor.class);
    }
}

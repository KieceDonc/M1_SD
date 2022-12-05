package com.perfect.bank.actors;

import java.util.ArrayList;

import com.perfect.bank.messages.Messages.DeclareAsBanker;
import com.perfect.bank.messages.Messages.Deposit;
import com.perfect.bank.messages.Messages.GetBalance;
import com.perfect.bank.messages.Messages.GetClientUID;
import com.perfect.bank.messages.Messages.IsClientExist;
import com.perfect.bank.messages.Messages.UnvalidateBanker;
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

    public BankActor() {
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(IsClientExist.class, message -> isClientExist(message))
                .match(GetClientUID.class, message -> getClientUID(message))
                .match(DeclareAsBanker.class, message -> registerBanker(getSender()))
                .match(Deposit.class, message -> clientDeposit(message))
                .match(Withdraw.class, message -> clientWithdraw(message))
                .match(GetBalance.class, message -> clientGetBalance(message))
                .match(UnvalidateBanker.class, message -> unvalidateBanker(getSender()))
                .build();
    }

    private void isClientExist(IsClientExist isClientExist) {
        this.getABanker().forward(isClientExist, getContext());
    }

    private void getClientUID(GetClientUID getClientUIDMessage) {
        this.getABanker().forward(getClientUIDMessage, getContext());
    }

    public void registerBanker(ActorRef banker) {
        // Banker isn't register yet
        if (bankers.lastIndexOf(banker) == -1) {
            bankers.add(banker);
            this.log.info("New banker register");
        }

    }

    public synchronized int generateUID() {
        return -1;
    }

    private void clientDeposit(Deposit depositMsg) {
        this.getABanker().forward(depositMsg, getContext());
    }

    private void clientWithdraw(Withdraw withdrawMsg) {
        this.getABanker().forward(withdrawMsg, getContext());
    }

    private void clientGetBalance(GetBalance getBalanceMsg) {
        this.getABanker().forward(getBalanceMsg, getContext());
    }

    private synchronized ActorRef getABanker() {
        if (indexOfLastBanker >= bankers.size()) {
            indexOfLastBanker = 0;
        }

        ActorRef banker = null;
        do {
            banker = bankers.get(indexOfLastBanker++);

            if (banker.isTerminated()) {
                bankers.remove(indexOfLastBanker);
                banker = null;
            }
        } while (banker == null);

        return banker;
    }

    private void unvalidateBanker(ActorRef banker) {
        bankers.remove(banker);
    }

    public static Props props() {
        return Props.create(BankActor.class);
    }
}

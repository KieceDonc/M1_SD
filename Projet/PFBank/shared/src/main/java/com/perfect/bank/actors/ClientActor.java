package com.perfect.bank.actors;

import akka.actor.ActorSelection;
import akka.actor.AbstractActor;
import akka.actor.Props;

import com.perfect.bank.helpers.Read;
import com.perfect.bank.messages.Messages;
import com.perfect.bank.messages.Messages.GetBalanceResponse;
import com.perfect.bank.messages.Messages.SetClientUID;

import akka.event.LoggingAdapter;
import akka.event.Logging;

public class ClientActor extends AbstractActor {

    private LoggingAdapter log = Logging.getLogger(this.getContext().system(), this);

    private ActorSelection bankActor;

    private int UID = -1;

    public ClientActor(ActorSelection bankActor, int clientUID) {
        this.bankActor = bankActor;
        this.UID = clientUID;
        this.loop();
    }

    public ClientActor(ActorSelection bankActor) {
        this.bankActor = bankActor;
        bankActor.tell(new Messages.GetClientUID(), getSelf());
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(SetClientUID.class, message -> setUID(message.getClientUID()))
                .match(GetBalanceResponse.class, message -> getBalanceResponse(message.getBalance()))
                .build();
    }

    private void setUID(int UID) {
        this.UID = UID;
        this.log.info("Client UID : " + this.UID);
        this.loop();
    }

    public int getUID() {
        return this.UID;
    }

    public void deposit(double amount) {
        bankActor.tell(new Messages.Deposit(this.getUID(), amount), getSelf());
    }

    public void withdraw(double amount) {
        bankActor.tell(new Messages.Withdraw(this.getUID(), amount), getSelf());
    }

    public void getBalance() {
        this.bankActor.tell(new Messages.GetBalance(this.getUID()), this.getSelf());
    }

    public void getBalanceResponse(double balance) {
        String menu = "----------------------------------\n";
        menu += "\tVotre solde : " + balance + "\n";
        menu += "----------------------------------\n";
        System.out.println(menu);
        this.loop();
    }

    public void loop() {
        showBankMenu();
        switch (Read.rInt()) {
            case 1: {
                deposit(this.getHowMuch());
                this.loop();
                break;
            }
            case 2: {
                withdraw(this.getHowMuch());
                this.loop();
                break;
            }
            case 3: {
                this.getBalance();
                break;
            }
            default: {
                System.exit(0);
            }
        }
    }

    public void showBankMenu() {
        String menu = "----------------------------------\n";
        menu += "\tVeuillez choisir une action\n";
        menu += "\t1 - Déposer\n";
        menu += "\t2 - Retirer\n";
        menu += "\t3 - Consulter votre solde\n";
        menu += "\t4 - Quitter\n";
        menu += "----------------------------------\n";
        System.out.println(menu);
    }

    public double getHowMuch() {
        String menu = "----------------------------------\n";
        menu += "\tVeuillez entrer le montant de votre opération :\n";
        menu += "----------------------------------\n";
        System.out.println(menu);
        return Read.rDouble();
    }

    public static Props props(ActorSelection bankActor) {
        return Props.create(ClientActor.class, bankActor);
    }

    public static Props props(ActorSelection bankActor, int clientUID) {
        return Props.create(ClientActor.class, bankActor, clientUID);
    }
}
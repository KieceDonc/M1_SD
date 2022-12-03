package com.perfect.bank.actor;

import akka.actor.ActorSelection;
import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.pattern.Patterns;
import java.time.Duration;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

import com.perfect.bank.classes.Read;
import com.perfect.bank.messages.Messages;
import com.perfect.bank.messages.Messages.Deposit;
import com.perfect.bank.messages.Messages.GetBalance;
import com.perfect.bank.messages.Messages.GetBalanceResponse;
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
        this.loop();
    }

    public ClientActor(ActorSelection bankActor) {
        this.bankActor = bankActor;
        this.getUID();
        this.loop();
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Deposit.class, message -> deposit(message.getAmount()))
                .match(Withdraw.class, message -> withdraw(message.getAmount()))
                .match(GetBalanceResponse.class, message -> getBalanceResponse(message.getBalance()))
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
        bankActor.tell(new Messages.Deposit(this.UID, amount), getSelf());
    }

    public void withdraw(double amount) {
        bankActor.tell(new Messages.Withdraw(this.UID, amount), getSelf());
    }

    public void getBalance() {
        this.bankActor.tell(new Messages.GetBalance(this.UID), this.getSelf());
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

    public int getHowMuch() {
        String menu = "----------------------------------\n";
        menu += "\tVeuillez entrer le montant de votre opération :\n";
        menu += "----------------------------------\n";
        System.out.println(menu);
        return Read.rInt();
    }

    public static Props props(ActorSelection bankActor) {
        return Props.create(ClientActor.class, bankActor);
    }

    public static Props props(ActorSelection bankActor, int clientUID) {
        return Props.create(ClientActor.class, bankActor, clientUID);
    }
}
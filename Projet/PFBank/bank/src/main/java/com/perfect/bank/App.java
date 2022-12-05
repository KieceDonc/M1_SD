package com.perfect.bank;

import com.perfect.bank.actors.BankActor;

import akka.actor.ActorSystem;

public class App {
    public static void main(String[] args) {
        ActorSystem actorSystem = ActorSystem.create("myActorSystem");
        actorSystem.actorOf(BankActor.props(), "bankActor");

        // En attente de Ctrl-C
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            actorSystem.terminate();
            System.out.println("System terminated.");
        }));
    }
}

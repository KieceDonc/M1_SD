package com.perfect.bank;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;

import com.perfect.bank.actor.BankActor;

public class App {
    public static void main(String[] args) {
        ActorSystem actorSystem = ActorSystem.create("myActorSystem");
        ActorRef bankActor = actorSystem.actorOf(BankActor.props(), "bankActor");

        // En attente de Ctrl-C
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            actorSystem.terminate();
            System.out.println("System terminated.");
        }));
    }
}

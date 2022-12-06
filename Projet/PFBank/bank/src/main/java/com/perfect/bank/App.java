package com.perfect.bank;

import com.perfect.bank.actors.BankActor;
import com.typesafe.config.ConfigFactory;

import akka.actor.ActorSystem;

public class App {
    public static void main(String[] args) {
        ActorSystem actorSystem = ActorSystem.create("myActorSystem", ConfigFactory.load("application.conf"));
        actorSystem.actorOf(BankActor.props(), "bankActor");

        // En attente de Ctrl-C
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            actorSystem.terminate();
            System.out.println("System terminated.");
        }));
    }
}

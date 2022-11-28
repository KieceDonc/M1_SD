package com.perfect.bank;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.ActorSystem;

import com.perfect.bank.actor.BankerActor;

import com.typesafe.config.ConfigFactory;

public class App {
    public static void main(String[] args) {
        ActorSystem actorSystem = ActorSystem.create("Banker", ConfigFactory.load("banker.conf"));
        ActorSelection bankActor = actorSystem.actorSelection("akka://myActorSystem@127.0.0.1:8000/user/bankActor");
        ActorRef bankerActor = actorSystem.actorOf(BankerActor.props(bankActor), "bankerActor");

        // En attente de Ctrl-C
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            actorSystem.terminate();
            System.out.println("System terminated.");
        }));

    }
}

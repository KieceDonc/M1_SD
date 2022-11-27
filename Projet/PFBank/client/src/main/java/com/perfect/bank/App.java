package com.perfect.bank;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.ActorSystem;

import com.perfect.bank.actor.ClientActor;

import com.typesafe.config.ConfigFactory;

public class App {
    public static void main(String[] args) {
        ActorSystem actorSystem = ActorSystem.create("Client", ConfigFactory.load("client.conf"));
        ActorSelection bankActor = actorSystem.actorSelection("akka://myActorSystem@127.0.0.1:8000/user/bankActor");
        ActorRef clientActor = actorSystem.actorOf(ClientActor.props(bankActor), "clientActor");

        // En attente de Ctrl-C
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            actorSystem.terminate();
            System.out.println("System terminated.");
        }));

    }
}

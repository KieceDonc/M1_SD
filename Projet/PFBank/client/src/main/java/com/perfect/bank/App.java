package com.perfect.bank;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import akka.pattern.Patterns;

import java.time.Duration;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

import com.perfect.bank.actor.ClientActor;
import com.perfect.bank.classes.Read;
import com.perfect.bank.messages.Messages;
import com.typesafe.config.ConfigFactory;

public class App {
    public static void main(String[] args) {
        ActorSystem actorSystem = ActorSystem.create("Client", ConfigFactory.load("client.conf"));
        ActorSelection bankActor = actorSystem.actorSelection("akka://myActorSystem@127.0.0.1:8000/user/bankActor");

        showMainMenu();
        switch (Read.rInt()) {
            case 1: {
                actorSystem.actorOf(ClientActor.props(bankActor, getUIDForAuth(bankActor)),
                        "clientActor");
                break;
            }
            case 2: {
                actorSystem.actorOf(ClientActor.props(bankActor), "clientActor");
                break;
            }
            default: {
                terminateActor(actorSystem);
                System.exit(0);
            }
        }

        // En attente de Ctrl-C
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            terminateActor(actorSystem);
        }));

    }

    public static void terminateActor(ActorSystem actorSystem) {
        actorSystem.terminate();
        System.out.println("System terminated.");
    }

    public static void showMainMenu() {
        String menu = "----------------------------------\n";
        menu += "\t1 - S'authentifier\n";
        menu += "\t2 - Créer un compte\n";
        menu += "\t3 - Quitter\n";
        menu += "----------------------------------\n";
        System.out.println(menu);
    }

    public static int getUIDForAuth(ActorSelection bankActor) {
        boolean UIDExist = false;
        String menu = "----------------------------------\n";
        menu += "\tVeuillez entrer votre UID :\n";
        menu += "----------------------------------\n";

        int UID = -1;
        do {

            System.out.println(menu);
            UID = Read.rInt();
            CompletionStage<Object> result = Patterns.ask(bankActor,
                    new Messages.IsClientExist(UID),
                    Duration.ofSeconds(10));

            try {
                UIDExist = (Boolean) result.toCompletableFuture().get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                System.exit(0);
            }

            if (!UIDExist) {
                System.out.println("UID invalide");
            }
        } while (!UIDExist);

        return UID;
    }
}

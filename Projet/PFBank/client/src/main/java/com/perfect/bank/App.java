package com.perfect.bank;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.ActorSystem;

import com.perfect.bank.actor.ClientActor;
import com.perfect.bank.classes.Read;
import com.perfect.bank.messages.Messages;
import com.typesafe.config.ConfigFactory;

public class App {
    public static void main(String[] args) {
        ActorSystem actorSystem = ActorSystem.create("Client", ConfigFactory.load("client.conf"));
        ActorSelection bankActor = actorSystem.actorSelection("akka://myActorSystem@127.0.0.1:8000/user/bankActor");
        ActorRef clientActor = null;

        showMainMenu();
        switch (Read.rInt()) {
            case 1: {
                actorSystem.actorOf(ClientActor.props(bankActor, getUIDForAuth()), "clientActor");
                break;
            }
            case 2: {
                clientActor = actorSystem.actorOf(ClientActor.props(bankActor), "clientActor");
                break;
            }
            default: {
                terminateActor(actorSystem);
                System.exit(0);
            }
        }

        showBankMenu();
        switch (Read.rInt()) {
            case 1: {
                clientActor.tell(new Messages.Deposit(-1, getHowMuch()), clientActor.noSender());
                break;
            }
            case 2: {
                clientActor.tell(new Messages.Withdraw(-1, getHowMuch()), clientActor.noSender());
                break;
            }
            case 3: {
                clientActor.tell(new Messages.GetBalance(), clientActor.noSender());
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
        menu += "1 - S'authentifier\n";
        menu += "2 - Créer un compte\n";
        menu += "3 - Quitter\n";
        menu += "----------------------------------\n";
        System.out.println(menu);
    }

    public static int getUIDForAuth() {
        String menu = "----------------------------------\n";
        menu += "Veuillez entrer votre UID :\n";
        menu += "----------------------------------\n";
        System.out.println(menu);
        return Read.rInt();
    }

    public static void showBankMenu() {
        String menu = "----------------------------------\n";
        menu += "Veuillez choisir une action\n";
        menu += "1 - Déposer\n";
        menu += "2 - Retirer\n";
        menu += "3 - Consulter votre solde\n";
        menu += "4 - Quitter\n";
        menu += "----------------------------------\n";
        System.out.println(menu);
    }

    public static int getHowMuch() {
        String menu = "----------------------------------\n";
        menu += "Veuillez entrer le montant de votre opération :\n";
        menu += "----------------------------------\n";
        System.out.println(menu);
        return Read.rInt();
    }
}

package com.perfect.bank;

import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import akka.pattern.Patterns;

import java.time.Duration;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

import com.perfect.bank.actors.ClientActor;
import com.perfect.bank.helpers.ConfigOverride;
import com.perfect.bank.helpers.Read;
import com.perfect.bank.messages.Messages;
import com.typesafe.config.ConfigFactory;

public class App {
    public static void main(String[] args) {
        // generateOneClientWithManualControl();
        generateClientsWithRandomAction(100);
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

    public static void generateOneClientWithManualControl() {
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

    public static void generateClientsWithRandomAction(int number) {
        if (number > 1899750000) {
            System.out.println("Le système n'est pas conçu pour autant de client");
            System.exit(0);
        }

        // port [10 000;60 000]
        // ip [127.51.X.X; 127.200.X.X]
        // total of possible clients 50 000 * 255 * 149 = 1 899 750 000
        for (int index = 0; index < number; index++) {
            int port = (index + 10000) % 60000;
            int rawHostID = (int) (index / 60000.0d);
            int _4Byte = rawHostID % 256;
            int _3Byte = (int) (rawHostID / 256.0d) % 256;
            int _2Byte = (int) (rawHostID / 256.0d / 256.0d) % 256 + 51;
            String ip = "127." + _2Byte + "." + _3Byte + "." + _4Byte;

            ActorSystem actorSystem = ActorSystem.create("Client",
                    ConfigOverride.ipAndPort(ConfigFactory.load("client.conf"), ip, "" + port));
            ActorSelection bankActor = actorSystem.actorSelection("akka://myActorSystem@127.0.0.1:8000/user/bankActor");

            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {

                }
            }, new Date(), 3000);

            // En attente de Ctrl-C
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                actorSystem.terminate();
                timer.cancel();
                System.out.println("System terminated.");
            }));
        }

    }
}

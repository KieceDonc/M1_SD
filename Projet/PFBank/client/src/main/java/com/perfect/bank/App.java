package com.perfect.bank;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import akka.pattern.Patterns;

import java.time.Duration;
import java.util.Date;
import java.util.Random;
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
        String menu = "----------------------------------\n";
        menu += "\t1 - Contrôle manuel sur 1 client\n";
        menu += "\t2 - Lance plusieurs clients\n";
        menu += "\t3 - Quitter\n";
        menu += "----------------------------------\n";
        System.out.println(menu);

        switch (Read.rInt()) {
            case 1: {
                generateClient();
                break;
            }
            case 2: {
                generateClients(getHowMuchClients());
                break;
            }
            default: {
                System.exit(0);
            }
        }
    }

    private static void terminateActor(ActorSystem actorSystem) {
        actorSystem.terminate();
        System.out.println("System terminated.");
    }

    private static void showMainMenu() {
        String menu = "----------------------------------\n";
        menu += "\t1 - S'authentifier\n";
        menu += "\t2 - Créer un compte\n";
        menu += "\t3 - Quitter\n";
        menu += "----------------------------------\n";
        System.out.println(menu);
    }

    private static int getUIDForAuth(ActorSelection bankActor) {
        boolean UIDExist = false;
        String menu = "----------------------------------\n";
        menu += "\tVeuillez entrer votre UID :\n";
        menu += "----------------------------------\n";

        int UID = -1;
        do {

            System.out.println(menu);
            UID = Read.rInt();
            UIDExist = isUIDExist(bankActor, UID);
            if (!UIDExist) {
                System.out.println("UID invalide");
            }
        } while (!UIDExist);

        return UID;
    }

    private static boolean isUIDExist(ActorSelection bankActor, int UID) {
        boolean UIDExist = true;
        CompletionStage<Object> result = Patterns.ask(bankActor,
                new Messages.IsClientExist(UID),
                Duration.ofSeconds(10));

        try {
            UIDExist = (Boolean) result.toCompletableFuture().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            System.exit(0);
        }

        return UIDExist;
    }

    public static void showBankMenu(ActorRef client) {
        String menu = "----------------------------------\n";
        menu += "\tVeuillez choisir une action\n";
        menu += "\t1 - Déposer\n";
        menu += "\t2 - Retirer\n";
        menu += "\t3 - Consulter votre solde\n";
        menu += "\t4 - Quitter\n";
        menu += "----------------------------------\n";

        System.out.println(menu);

        switch (Read.rInt()) {
            case 1: {
                client.tell(new Messages.Deposit(-1, getHowMuch()), ActorRef.noSender());
                showBankMenu(client);
                break;
            }
            case 2: {
                client.tell(new Messages.Withdraw(-1, getHowMuch()), ActorRef.noSender());
                showBankMenu(client);
                break;
            }
            case 3: {
                // client.tell(new Messages.GetBalance(-1), ActorRef.noSender());
                getBalance(client, "127.1.0.0", "10000", "\tDemande consultation du solde\n");
                showBankMenu(client);
                break;
            }
            default: {
                System.exit(0);
            }
        }
    }

    private static int getHowMuchClients() {
        String menu = "----------------------------------\n";
        menu += "\tVeuillez entrer le nombre de client :\n";
        menu += "----------------------------------\n";
        System.out.println(menu);
        return Read.rInt();
    }

    private static double getHowMuch() {
        String menu = "----------------------------------\n";
        menu += "\tVeuillez entrer le montant de votre opération :\n";
        menu += "----------------------------------\n";
        System.out.println(menu);
        return Read.rDouble();
    }

    private static void getBalance(ActorRef client, String ip, String port, String menuOperationInfo) {
        double balance = 0.0d;
        CompletionStage<Object> result = Patterns.ask(client, new Messages.GetBalance(-1),
                Duration.ofSeconds(10));

        try {
            balance = (Double) result.toCompletableFuture().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        String menu = "----------------------------------\n";
        menu += "\tVotre solde : " + balance + "\n";
        menu += "\tDe : " + ip + ":" + port + "\n";
        menu += menuOperationInfo;
        menu += "----------------------------------\n";
        System.out.println(menu);
    }

    private static void generateClient() {
        ActorSystem actorSystem = ActorSystem.create("Client", ConfigFactory.load("client.conf"));
        ActorSelection bankActor = actorSystem.actorSelection("akka://myActorSystem@127.0.0.1:8000/user/bankActor");
        ActorRef client = null;

        showMainMenu();
        switch (Read.rInt()) {
            case 1: {
                client = actorSystem.actorOf(ClientActor.props(bankActor, getUIDForAuth(bankActor)),
                        "clientActor");
                break;
            }
            case 2: {
                client = actorSystem.actorOf(ClientActor.props(bankActor), "clientActor");
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

        showBankMenu(client);
    }

    private static void generateClients(int number) {
        if (number > 1899750000) {
            System.out.println("Le système n'est pas conçu pour autant de client");
            System.exit(0);
        }

        // port [10 000;60 000]
        // ip [127.51.X.X; 127.200.X.X]
        // total of possible clients 50 000 * 255 * 149 = 1 899 750 000
        for (int index = 0; index < number; index++) {
            final int port = (index + 10000) % 60000;
            int rawHostID = (int) (index / 60000.0d);
            int _4Byte = rawHostID % 256;
            int _3Byte = (int) (rawHostID / 256.0d) % 256;
            int _2Byte = (int) (rawHostID / 256.0d / 256.0d) % 256 + 51;
            final String ip = "127." + _2Byte + "." + _3Byte + "." + _4Byte;

            final int UID = index;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String name = "Client" + UID;
                    ActorSystem actorSystem = ActorSystem.create(name,
                            ConfigOverride.ipAndPort(ConfigFactory.load("client.conf"), ip, "" + port));
                    ActorSelection bankActor = actorSystem
                            .actorSelection("akka://myActorSystem@127.0.0.1:8000/user/bankActor");
                    final ActorRef client;

                    if (isUIDExist(bankActor, UID)) {
                        client = actorSystem.actorOf(ClientActor.props(bankActor, UID));
                    } else {
                        client = actorSystem.actorOf(ClientActor.props(bankActor));
                    }

                    Timer timer = new Timer();
                    timer.schedule(new TimerTask() {
                        double amount = 10 + (10000 - 10) * new Random().nextDouble();

                        @Override
                        public void run() {
                            switch ((int) (Math.random() * 2)) {
                                case 0: {
                                    client.tell(new Messages.Deposit(-1, amount), ActorRef.noSender());
                                    getBalance(client, ip, String.valueOf(port), "\tDépôt de " + amount + "\n");
                                    break;
                                }
                                case 1: {
                                    client.tell(new Messages.Withdraw(-1, amount), ActorRef.noSender());
                                    getBalance(client, ip, String.valueOf(port), "\tRetrait de " + amount + "\n");
                                    break;
                                }
                            }
                        }
                    }, new Date(), 3000);

                    // En attente de Ctrl-C
                    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                        actorSystem.terminate();
                        timer.cancel();
                        System.out.println("System terminated.");
                    }));
                }
            }).start();
        }

    }
}

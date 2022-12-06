package com.perfect.bank;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.ActorSystem;

import com.perfect.bank.actors.BankerActor;
import com.perfect.bank.helpers.ConfigOverride;
import com.perfect.bank.helpers.Read;
import com.perfect.bank.messages.Messages;
import com.typesafe.config.ConfigFactory;

public class App {
    public static void main(String[] args) {
        String menu = "----------------------------------------------------------------------\n";
        menu += "\tNombre de banquiers pour la banque : \n";
        menu += "----------------------------------------------------------------------\n";
        System.out.println(menu);

        generateBankers(Read.rInt());

    }

    public static void generateBankers(int number) {
        if (number > 624750000) {
            System.out.println("Le système n'est pas conçu pour autant de banquier");
            System.exit(0);
        }

        // port [1 000;60 000]
        // ip [127.1.X.X; 127.50.X.X]
        // total of possible bankers 50 000 * 255 * 49 = 624 750 000
        for (int index = 0; index < number; index++) {
            int port = (index + 10000) % 60000;
            int rawHostID = (int) (index / 60000.0d);
            int _4Byte = rawHostID % 256;
            int _3Byte = (int) (rawHostID / 256.0d) % 256;
            int _2Byte = (int) (rawHostID / 256.0d / 256.0d) % 256 + 1;
            String ip = "127." + _2Byte + "." + _3Byte + "." + _4Byte;

            System.out.println(ip + ":" + port);
            ActorSystem actorSystem = ActorSystem.create("Banker",
                    ConfigOverride.ipAndPort(ConfigFactory.load("banker.conf"), ip, "" + port));
            ActorSelection bankActor = actorSystem
                    .actorSelection("akka://myActorSystem@127.0.0.1:8000/user/bankActor");
            ActorRef bankerActor = actorSystem.actorOf(BankerActor.props(bankActor), "bankerActor");

            // En attente de Ctrl-C
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                bankerActor.tell(new Messages.Shutdown(), ActorRef.noSender());
                actorSystem.terminate();
                System.out.println("Banker terminated.");
            }));
        }
    }
}

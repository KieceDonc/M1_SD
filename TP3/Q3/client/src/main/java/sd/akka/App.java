package sd.akka;

import java.time.Duration;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import akka.pattern.Patterns;
import akka.routing.RoundRobinPool;
import com.typesafe.config.ConfigFactory;

import sd.akka.actor.ArbitreActor;

public class App {
    public static void main(String[] args) {
        ActorSystem actorSystem = ActorSystem.create("Client", ConfigFactory.load("client.conf"));
        ActorSelection arbitreActor = actorSystem
                .actorSelection("akka://myActorSystem@127.0.0.1:8000/user/ArbitreActor");

        arbitreActor.tell(new ArbitreActor.GetScore(), ActorRef.noSender());
        CompletionStage<Object> result = Patterns.ask(arbitreActor, new ArbitreActor.StartGame(),
                Duration.ofSeconds(10));
        try {
            result.toCompletableFuture().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        arbitreActor.tell(new ArbitreActor.GetScore(), ActorRef.noSender());

        // Arrêt du système d'acteurs
        actorSystem.terminate();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            actorSystem.terminate();
            System.out.println("System terminated.");
        }));
    }
}

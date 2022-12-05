package sd.akka;

import java.time.Duration;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.pattern.Patterns;
import akka.routing.RoundRobinPool;
import sd.akka.actor.ArbitreActor;

public class App {
	public static void main(String[] args) {
		ActorSystem actorSystem = ActorSystem.create("myActorSystem");
		ActorRef arbitreActor = actorSystem.actorOf(ArbitreActor.props(), "arbitre");

		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			arbitreActor.terminate();
			System.out.println("System terminated.");
		}));

	}
}

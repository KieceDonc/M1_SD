package sd.akka;

import java.time.Duration;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.pattern.Patterns;
import akka.routing.RoundRobinPool;
import sd.akka.actor.DefaultActor;

public class App {
	public static void main(String[] args) {
        ActorSystem actorSystem = ActorSystem.create();
		ActorRef defaultActor = actorSystem.actorOf(DefaultActor.props());
		
        defaultActor.tell(new DefaultActor.CreateChild(20), ActorRef.noSender());
        defaultActor.tell(new DefaultActor.ChangeString("Ceci est une chaîne de caractère"), ActorRef.noSender());
    }
}

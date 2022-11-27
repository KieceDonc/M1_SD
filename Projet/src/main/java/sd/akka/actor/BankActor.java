package sd.akka.actor;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.pattern.Patterns;
import java.time.Duration;
import java.util.concurrent.CompletionStage;
import akka.event.LoggingAdapter;
import akka.event.Logging;
public class BankActor extends AbstractActor {

    private LoggingAdapter log = Logging.getLogger(this.getContext().system(), this);

    private int clientLastUID = 0;

    public BankActor(){
        
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder().match(GetUID.class, message -> getUID()).build();
    }

    public synchronized int getUID() {
        return this.clientLastUID++;
    }

    public static Props props() {
        return Props.create(BankActor.class);
    }

    public class GetBanker {

    }

    public class ClientBalance {

    }

    public class GetUID {

    }

}

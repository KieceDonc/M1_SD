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

public class ClientActor extends AbstractActor {

    private LoggingAdapter log = Logging.getLogger(this.getContext().system(), this);

    private ActorRef bankActor;

    private int UID = -1;

    public ClientActor(ActorRef bankActor) {
        this.bankActor = bankActor;
        this.getUID();
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder().build();
    }

    public int getUID() {
        if (this.UID < 0) {
            CompletionStage<Object> result = Patterns.ask(bankActor,
                    new BankActor.GetUID(),
                    Duration.ofSeconds(10));
            this.UID = (int) result.toCompletableFuture().get();
            System.out.println("[Client] UID reçu : " + this.UID);
        }

        return this.UID;
    }

    public static Props props(ActorRef bankActor) {
        return Props.create(ClientActor.class, bankActor);
    }

    public class GetBanker {

    }

    public class Withdraw {

        public Withdraw(double amount) {
            super(amount);
        }
    }

    public class Deposit extends BankOperation {

        public Deposit(double amount) {
            super(amount);
        }
    }

    private class BankOperation {

        private double amount;

        public BankOperation(double amount) {
            this.amount = amount;
        }

        public double getAmount() {
            return this.amount;
        }
    }
}
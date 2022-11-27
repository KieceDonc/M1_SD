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
public class BankerActor extends AbstractActor {

    private LoggingAdapter log = Logging.getLogger(this.getContext().system(), this);

    public BankerActor(){

    }

    @Override
    public Receive createReceive() {
        return receiveBuilder().build();
    }

    public static Props props() {
        return Props.create(BankerActor.class);
    }

    public class GetBalance {

        private int clientID;

        public GetBalance(int clientID) {
            this.clientID = clientID;
        }

        public int getClientID(){
            return this.clientID;
        }
    }

    public class Withdraw extends BankOperation {

        public Withdraw(int clientID, double amount) {
            super(clientID, amount);
        }
    }

    public class Deposit extends BankOperation {

        public Deposit(int clientID, double amount) {
            super(clientID, amount);
        }
    }

    private class BankOperation {

        private int clientID;
        private double amount;

        public BankOperation(int clientID, double amount) {
            this.clientID = clientID;
            this.amount = amount;
        }

        public int getClientID() {
            return this.clientID;
        }

        public double getAmount() {
            return this.amount;
        }
    }

}

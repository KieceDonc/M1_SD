package sd.akka.actor;

import java.util.Random;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.pattern.Patterns;
import java.time.Duration;
import java.util.concurrent.CompletionStage;
import akka.event.LoggingAdapter;
import akka.event.Logging;

public class StockActor extends AbstractActor {

    private int numberOfFruits = 0;

    private LoggingAdapter log = Logging.getLogger(this.getContext().system(), this);

    private StockActor() {
        this.numberOfFruits = 100;
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(OrderReception.class, message -> orderReception(message, getSender()))
                .build();
    }

    public void orderReception(final OrderReception message, ActorRef actor) {
        int numberOfFruitsOrder = message.getNumberOfFruitsOrder();
        System.out.println("New order received, number of fruits ordered : " + numberOfFruitsOrder
                + ", current stock : " + this.numberOfFruits);
        if (this.numberOfFruits > numberOfFruitsOrder) {
            System.out.println("Sending order to client, number of fruits : " + numberOfFruitsOrder);
            this.deStock(numberOfFruitsOrder);
            actor.tell(numberOfFruitsOrder, this.getSelf());
            this.reStock(numberOfFruitsOrder);
        } else {
            System.out.println("Unable to send or to client, number of fruits : " + numberOfFruitsOrder);
            actor.tell(0, this.getSelf());
            this.reStock(numberOfFruitsOrder);
        }
    }

    private void deStock(int numberOfFruitsToDeStock) {
        System.out.println("deStock of " + numberOfFruitsToDeStock + " fruits");
        this.numberOfFruits -= numberOfFruitsToDeStock;
    }

    private void reStock(int numberOfFruitsToReStock) {

        StockActor stockActor = this;
        Thread reStockThread = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    int reStockDelay = (int) (Math.random() * (10000 - 3000)) + 3000;
                    int numberOfFruitsReceived = (int) ((double) numberOfFruitsToReStock * 1.2
                            - (int) (Math.random()
                                    * ((double) numberOfFruitsToReStock * 0.2 - (double) numberOfFruitsToReStock * 0.2))
                            + (double) numberOfFruitsToReStock * 0.2);
                    Thread.sleep(reStockDelay);
                    stockActor.numberOfFruits += numberOfFruitsReceived;

                    System.out.println("reStock of " + numberOfFruitsReceived + " fruits");
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        });

        reStockThread.start();
    }

    public static Props props() {
        return Props.create(StockActor.class);
    }

    public static class OrderReception {
        private int numberOfFruitsOrder;

        public OrderReception(int numberOfFruitsOrder) {
            this.numberOfFruitsOrder = numberOfFruitsOrder;
        }

        public int getNumberOfFruitsOrder() {
            return this.numberOfFruitsOrder;
        }
    }
}
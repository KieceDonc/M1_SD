package sd.akka;

import java.time.Duration;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.CompletionStage;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.pattern.Patterns;
import akka.routing.RoundRobinPool;
import sd.akka.actor.BankActor;
import sd.akka.actor.ClientActor;
import akka.pattern.Patterns;
import java.time.Duration;

public class App {

    // mvn exec:java -Dexec.mainClass=sd.akka.App
    public static void main(String[] args) {
        /*
         * try {
         * 
         * Thread makeOrdersThread = new Thread(new Runnable() {
         * 
         * @Override
         * public void run() {
         * try {
         * 
         * ActorSystem actorSystem = ActorSystem.create();
         * ActorRef stockActor = actorSystem.actorOf(StockActor.props());
         * 
         * for (int index = 0; index < 100; index++) {
         * 
         * int numberOfFruitsOrder = (int) (Math.random() * (400 - 300) + 300);
         * 
         * CompletionStage<Object> result = Patterns.ask(stockActor,
         * new StockActor.OrderReception(numberOfFruitsOrder),
         * Duration.ofSeconds(30));
         * int fruitsReceived = (int) result.toCompletableFuture().get();
         * System.out.println("Number of fruits received : " + fruitsReceived);
         * 
         * int orderDelay = (int) (Math.random() * (8000 - 2000)) + 2000;
         * Thread.sleep(orderDelay);
         * }
         * } catch (InterruptedException | ExecutionException e) {
         * System.out.println(e);
         * }
         * 
         * }
         * });
         * 
         * makeOrdersThread.start();
         * } catch (Exception e) {
         * e.printStackTrace();
         * }
         */

        ActorSystem actorSystem = ActorSystem.create();
        ActorRef bankActor = actorSystem.actorOf(BankActor.props());
        ActorRef clientActor = actorSystem.actorOf(ClientActor.props(bankActor));
    }
}

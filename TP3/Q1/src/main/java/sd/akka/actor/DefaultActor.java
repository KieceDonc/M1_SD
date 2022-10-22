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

public class DefaultActor extends AbstractActor {

    private LoggingAdapter log = Logging.getLogger(this.getContext().system(), this);

    private ActorRef child;

    private DefaultActor() {
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(CreateChild.class, message -> createChild(message))
                .match(ChangeString.class, message -> changeString(message, getSender()))
                .build();
    }

    public void createChild(final CreateChild message) {
        int childLeft = message.getChildLeft() - 1;
        if (childLeft > 0) {
            ActorSystem actorSystem = ActorSystem.create();
            ActorRef defaultActor = actorSystem.actorOf(DefaultActor.props());

            defaultActor.tell(new DefaultActor.CreateChild(childLeft), ActorRef.noSender());
            this.child = defaultActor;
        } else {
            child = null;
        }
    }

    public void changeString(final ChangeString message, ActorRef actor) {
        try {
            char[] messageChars = message.getString().toCharArray();
            int indexToReplace = new Random().nextInt(message.getString().length());
            messageChars[indexToReplace] = (char) (new Random().nextInt(26) + 'a');
            String newMessage = String.valueOf(messageChars);

            if (child != null) {
                CompletionStage<Object> result = Patterns.ask(child, new DefaultActor.ChangeString(newMessage),
                        Duration.ofSeconds(10));
                newMessage = (String) result.toCompletableFuture().get();
            }
            actor.tell(newMessage, this.getSelf());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Props props() {
        return Props.create(DefaultActor.class);
    }

    public static class CreateChild {
        private int childLeft;

        public CreateChild(int childLeft) {
            this.childLeft = childLeft;
        }

        public int getChildLeft() {
            return this.childLeft;
        }
    }

    public static class ChangeString {
        private String string;

        public ChangeString(String string) {
            this.string = string;
        }

        public String getString() {
            return this.string;
        }
    }
}
package sd.akka.actor;

import java.util.Random;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.AbstractActor;
import akka.actor.Props;

public class DefaultActor extends AbstractActor {

    private ActorRef child;

    private DefaultActor(){}

    @Override
    public Receive createReceive(){
        return receiveBuilder()
            .match(CreateChild.class, message -> createChild(message))
            .match(ChangeString.class, message -> changeString(message))
            .build();
    }

    public void createChild(final CreateChild message){
        int childLeft = message.getChildLeft() - 1;
        if(childLeft > 0){
            ActorSystem actorSystem = ActorSystem.create();
            ActorRef defaultActor = actorSystem.actorOf(DefaultActor.props());
            
            defaultActor.tell(new DefaultActor.CreateChild(childLeft), ActorRef.noSender());
            this.child = defaultActor;
        }else{
            child = null;
        }
    }

    public void changeString(final ChangeString message){
        char[] messageChars = message.getString().toCharArray();
        int indexToReplace = new Random().nextInt(message.getString().length() + 1);
        messageChars[indexToReplace] = (char)(new Random().nextInt(26) + 'a');
        String newMessage = String.valueOf(messageChars);

        this.child.tell(new DefaultActor.ChangeString(newMessage), ActorRef.noSender());
        System.out.println("Message : "+newMessage);
    }

    public static Props props(){
        return Props.create(DefaultActor.class);
    }

    public static class CreateChild {
        private int childLeft;
        private ActorRef acestor;

        public CreateChild(int childLeft){
            this.childLeft = childLeft;
        }

        public int getChildLeft(){
            return this.childLeft;
        }
    }

    public static class ChangeString {
        private String string;

        public ChangeString(String string){
            this.string = string;
        }

        public String getString(){
            return this.string;
        }
    }
}
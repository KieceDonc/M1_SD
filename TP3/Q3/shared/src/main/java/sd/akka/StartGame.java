package sd.akka;

import akka.actor.ActorRef;
import java.io.Serializable;

public class StartGame implements Serializable {
    public ActorRef adversaire;

    public StartGame(ActorRef adversaire) {
        this.adversaire = adversaire;
    }
}

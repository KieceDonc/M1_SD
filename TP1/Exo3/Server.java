import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {

    public static void main(String[] args) {
        try {
            BagOfTaskImp bagOfTask = new BagOfTaskImp();

            Registry registry = LocateRegistry.getRegistry();
            registry.bind("BagOfTask", bagOfTask);
        } catch (RemoteException | AlreadyBoundException e) {
            e.printStackTrace();
        }
    }
}

package myPackage;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {

    public static void main(String[] args) {
        try {
            BagOfTaskImp bagOfTask = new BagOfTaskImp();

            for (int index = 0; index < 1000; index++) {
                bagOfTask.addTask(new TaskImp(bagOfTask, index));
            }

            Registry registry = LocateRegistry.getRegistry();
            registry.bind("BagOfTask", registry);
        } catch (RemoteException | AlreadyBoundException e) {
            e.printStackTrace();
        }
    }
}

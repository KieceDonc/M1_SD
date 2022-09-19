import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client {
  public static void main(String arg[]) {
    try {

      Registry registry = LocateRegistry.getRegistry("127.0.0.1");
      BagOfTask stub = (BagOfTask) registry.lookup("BagOfTask");

      Task task = stub.nextTask();
      task.execute();

      try {
        stub.sendResult(task);
      } catch (RemoteException e) {
        e.printStackTrace();
      }

    } catch (Exception e) {
      System.err.println("Erreur :" + e);
    }
  }
}
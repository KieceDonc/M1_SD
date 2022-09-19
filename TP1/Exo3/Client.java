import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client extends Thread {
  public static void main(String arg[]) {
    try {

      Registry registry = LocateRegistry.getRegistry("127.0.0.1");
      BagOfTask stub = (BagOfTask) registry.lookup("BagOfTask");

      for (int index = 0; index < 200; index++) {
        Task task = stub.nextTask();

        if (task != null) {
          task.execute();

          try {
            stub.sendResult(task);
          } catch (RemoteException e) {
            e.printStackTrace();
          }

        } else {
          sleep(1000);
        }
      }
    } catch (Exception e) {
      System.err.println("Erreur :" + e);
    }
  }
}
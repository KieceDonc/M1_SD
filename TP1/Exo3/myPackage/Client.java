package myPackage;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client extends Thread {
  public static void main(String arg[]) {
    try {

      Registry registry = LocateRegistry.getRegistry("127.0.0.1");
      BagOfTask skeleton = (BagOfTask) registry.lookup("BagOfTask");

      Task task = skeleton.nextTask();
      task.execute();

    } catch (Exception e) {
      System.err.println("Erreur :" + e);
    }
  }
}
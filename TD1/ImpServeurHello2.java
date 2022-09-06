import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Serveur {
public static void main( String [] args) {
try {
    ODImpl unOD = new ODImpl ();
    Registry registry = LocateRegistry.createRegistry (1099);
    //Registry registry = LocateRegistry.getRegistry ();
    registry.bind("MonOD",unOD);
    System.out.println("Serveur pret"); }
    catch (Exception e) { System.out.println(e) ; }
}
}
import java.rmi.registry.Registry;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

public class ImpServeurHello implements Compteur {
    // rmiregistry &
    // java ImpServeurHello

    public ImpServeurHello() {
    }

    @Override
    public int Somme(int a, int b) throws RemoteException {
        return a + b;
    }

    public static void main(String args[]) {

        try {
            ImpServeurHello obj = new ImpServeurHello();
            Registry registry = LocateRegistry.getRegistry();
            Compteur stub = (Compteur) UnicastRemoteObject.exportObject(obj, 0);

            registry.rebind("Somme", stub);

            System.err.println("Server ready");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }
}
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ImpServeurHello implements Compteur {

    public ImpServeurHello() {
    }

    @Override
    public int Somme(int a, int b) throws RemoteException {
        return a + b;
    }

    public static void main(String args[]) {

        try {
            ImpServeurHello obj = new ImpServeurHello();
            Compteur nom = (Compteur) UnicastRemoteObject.exportObject(obj, 0);

            Naming.rebind("Somme", nom);

            System.err.println("Server ready");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }
}
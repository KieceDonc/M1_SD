import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ClientHello {
    public static void main(String arg[]) {
        try {
            int a = 5;
            int b = 7;

            Registry registry = LocateRegistry.getRegistry("0.0.0.0");
            Compteur stub = (Compteur) registry.lookup("Somme");
            int result = stub.addition(a, b);

            Compteur c = (Compteur) Naming.lookup("Somme");
            int result = c.Somme(a, b);
            System.out.println("Résultat " + String.valueOf(result));
        } catch (Exception e) {
            System.err.println("Erreur :" + e);
        }
    }
}
package TP1.Exo2;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client {
    public static void main(String arg[]) {
        try {
            int a = 5;
            int b = 7;

            Registry registry = LocateRegistry.getRegistry("127.0.0.1");
            Compteur stub = (Compteur) registry.lookup("Somme");

            int result = stub.Somme(a, b);
            System.out.println("Résultat " + String.valueOf(result));
        } catch (Exception e) {
            System.err.println("Erreur :" + e);
        }
    }
}
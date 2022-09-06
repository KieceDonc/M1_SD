import java.rmi.*;

public class ClientHello {
    public static void main(String arg[]) {
        try {
            int a = 5;
            int b = 7;

            Compteur c = (Compteur) Naming.lookup("Somme");
            int result = c.Somme(a, b);
            System.out.println("Résultat " + String.valueOf(result));
        } catch (Exception e) {
            System.err.println("Erreur :" + e);
        }
    }
}
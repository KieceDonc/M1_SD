import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client {
    public static void main(String arg[]) {
        try {

            Registry registry = LocateRegistry.getRegistry("192.168.1.77");

            AccountFactory stub = (AccountFactory) registry.lookup("AccountFactory");
            Account account = stub.createAccount();

            System.out.println("Balance : " + String.valueOf(account.getBalance()));
            account.deposit(10.25f);
            System.out.println("Balance : " + String.valueOf(account.getBalance()));
            account.withdraw(8.75f);
            System.out.println("Balance : " + String.valueOf(account.getBalance()));
        } catch (Exception e) {
            System.err.println("Erreur :" + e);
        }
    }
}
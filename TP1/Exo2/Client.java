import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Random;

public class Client extends Thread {
    public static void main(String arg[]) {
        try {

            Registry registry = LocateRegistry.getRegistry("127.0.0.1");

            AccountFactory stub = (AccountFactory) registry.lookup("AccountFactory");

            for (int x = 0; x < 1000; x++) {
                Account account = stub.getAccount();

                System.out.println("-------------------------------------------------------------");
                printAccountBalance(account);
                account.deposit(getRandomValue(100, 200));
                printAccountBalance(account);
                account.withdraw(getRandomValue(50, 90));
                printAccountBalance(account);
                System.out.println(
                        "-------------------------------------------------------------");
            }
        } catch (Exception e) {
            System.err.println("Erreur :" + e);
        }
    }

    private static double getRandomValue(double min, double max) {
        double value = min + new Random().nextDouble() * (max - min);
        return (double) ((int) (value * 100f)) / 100f;

    }

    private static void printAccountBalance(Account account) {
        try {
            System.out.println(
                    "Account ID : " + account.getID() + " - Balance : " + String.valueOf(account.getBalance()));
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }
}
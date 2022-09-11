import java.math.RoundingMode;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.text.DecimalFormat;
import java.util.Random;

public class Client extends Thread {
    public static void main(String arg[]) {
        try {

            Registry registry = LocateRegistry.getRegistry("192.168.1.77");

            AccountFactory stub = (AccountFactory) registry.lookup("AccountFactory");

            for (int x = 0; x < 10000; x++) {
                Account account = stub.createAccount();

                System.out.println("-------------------------------------------------------------");
                printAccountBalance(account);
                account.deposit(getRandomValue(100, 200));
                printAccountBalance(account);
                account.withdraw(getRandomValue(50, 90));
                printAccountBalance(account);
                System.out.println(
                        "Sleeping for 5ms ZzZzzzz\n-------------------------------------------------------------");

                Thread.sleep(5);
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
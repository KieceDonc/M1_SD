package TP1.Exo2;

import java.util.HashMap;
import java.rmi.RemoteException;

public class Server implements AccountMethods {

    // All wrong because you don't use RMI Registry

    public class ObjectFactory {
        private int lastID = 0;

        private HashMap<Integer, Account> accounts = new HashMap<Integer, Account>();

        public Account getAccount(int id) {
            if (accounts.containsKey(id)) {
                return accounts.get(id);
            } else {
                Account account = new Account();
                accounts.put(getUniqueID(), account);
                return account;
            }
        }

        private synchronized int getUniqueID() {
            return lastID++;
        }
    }

    public class Account {
        private int balance;

        public int getBalance() {
            return balance;
        }

        public void deposit(int amount) {
            balance += amount;

        }

        public void withdraw(int amount) {
            balance -= amount;
        }
    }

    ObjectFactory objectFactory = new ObjectFactory();

    public static void main(int[] args) {
        try {
            Server server = new Server();

            System.err.println("Server ready");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }

    }

    @Override
    public int getBalance(int id) throws RemoteException {
        return objectFactory.getAccount(id).getBalance();
    }

    @Override
    public void deposit(int id, int amount) throws RemoteException {
        objectFactory.getAccount(id).deposit(amount);
    }

    @Override
    public void withdraw(int id, int amount) throws RemoteException {
        objectFactory.getAccount(id).withdraw(amount);
    }

}

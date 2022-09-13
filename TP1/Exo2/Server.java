import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

public class Server {

    public static class AccountImp extends UnicastRemoteObject implements Account {
        private double balance;
        private int ID;

        protected AccountImp(int ID) throws RemoteException {
            super();
            this.ID = ID;
        }

        @Override
        public double getBalance() throws RemoteException {
            return balance;
        }

        @Override
        public void deposit(double amount) throws RemoteException {
            balance += amount;
        }

        @Override
        public void withdraw(double amount) throws RemoteException {
            balance -= amount;
        }

        @Override
        public int getID() throws RemoteException {
            return ID;
        }
    }

    public static class AccountFactoryImp extends UnicastRemoteObject implements AccountFactory {
        // https://docs.oracle.com/javase/8/docs/technotes/guides/rmi/Factory.html

        private HashMap<Integer, AccountImp> Accounts;
        private int lastUniqueID;

        protected AccountFactoryImp() throws RemoteException {
            super();
            Accounts = new HashMap<>();
        }

        @Override
        public Account getAccount(int ID) throws RemoteException {
            if (Accounts.containsKey(ID)) {
                return Accounts.get(ID);
            } else {
                AccountImp account = new AccountImp(getUniqueID());
                Accounts.put(account.getID(), account);
                return (Account) account;
            }
        }

        private synchronized int getUniqueID() {
            return lastUniqueID++;
        }
    }

    public static void main(String[] args) {
        try {
            AccountFactoryImp stub = new AccountFactoryImp();
            Registry registry = LocateRegistry.getRegistry();
            registry.bind("AccountFactory", stub);

        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }

    }

}

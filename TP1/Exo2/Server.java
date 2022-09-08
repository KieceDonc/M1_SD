import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Server {

    public static class AccountImp extends UnicastRemoteObject implements Account {
        private float balance;

        protected AccountImp() throws RemoteException {
            super();
        }

        @Override
        public float getBalance() throws RemoteException {
            return balance;
        }

        @Override
        public void deposit(float amount) throws RemoteException {
            balance += amount;
        }

        @Override
        public void withdraw(float amount) throws RemoteException {
            balance -= amount;
        }
    }

    public static class AccountFactoryImp extends UnicastRemoteObject implements AccountFactory {

        protected AccountFactoryImp() throws RemoteException {
            super();
        }

        @Override
        public Account createAccount() throws RemoteException {
            return new AccountImp();
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

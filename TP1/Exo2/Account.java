import java.rmi.Remote;

public interface Account extends Remote {
    public double getBalance() throws java.rmi.RemoteException;

    public void deposit(double amount) throws java.rmi.RemoteException;

    public void withdraw(double amount) throws java.rmi.RemoteException;

    public int getID() throws java.rmi.RemoteException;
}
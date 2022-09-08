import java.rmi.Remote;

public interface Account extends Remote {
    public float getBalance() throws java.rmi.RemoteException;

    public void deposit(float amount) throws java.rmi.RemoteException;

    public void withdraw(float amount) throws java.rmi.RemoteException;
}
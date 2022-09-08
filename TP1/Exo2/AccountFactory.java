import java.rmi.Remote;

public interface AccountFactory extends Remote {
    public Account createAccount() throws java.rmi.RemoteException;
}

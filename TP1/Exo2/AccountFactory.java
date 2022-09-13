import java.rmi.Remote;

public interface AccountFactory extends Remote {
    public Account getAccount(int ID) throws java.rmi.RemoteException;
}

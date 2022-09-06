import java.rmi.Remote;

public interface Hello extends Remote {
    public String ditBonjour() throws java.rmi.RemoteException;
}
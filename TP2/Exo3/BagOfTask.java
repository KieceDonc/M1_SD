import java.rmi.Remote;

public interface BagOfTask extends Remote {

    public Task nextTask() throws java.rmi.RemoteException;

    public void sendResult(Task task) throws java.rmi.RemoteException;
}

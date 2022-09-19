package myPackage;

public interface BagOfTask {
    public void addTask(Task task) throws java.rmi.RemoteException;

    public Task nextTask() throws java.rmi.RemoteException;

    public void sendResult(Task task) throws java.rmi.RemoteException;
}

public interface BaskOfTask {
    public Task nextTask() throws java.rmi.RemoteException;
    public void sendResult(Task task) throws java.rmi.RemoteException;  
}

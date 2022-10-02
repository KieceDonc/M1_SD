import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedList;
import java.util.Queue;

public class BagOfTaskImp extends UnicastRemoteObject implements BagOfTask {
    Queue<TaskImp> tasks = new LinkedList<>();

    public BagOfTaskImp() throws RemoteException {
        for (int index = 0; index < 1000; index++) {
            tasks.add(new TaskImp(index));
        }
    }

    @Override
    public Task nextTask() throws RemoteException {
        if (tasks.isEmpty()) {
            return null;
        } else {
            return (Task) tasks.remove();
        }
    }

    @Override
    public void sendResult(Task task) throws RemoteException {
        String toPrint = "";

        boolean isPrimeNumber = task.isPrimeNumber();

        if (isPrimeNumber) {
            toPrint = task.getNumber() + " est un nombre premier";
        } else {
            toPrint = task.getNumber() + " n'est pas un nombre premier";
        }

        System.out.println(toPrint);
    }

}

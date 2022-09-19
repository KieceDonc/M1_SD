package myPackage;

import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.Queue;

public class BagOfTaskImp implements BagOfTask {
    Queue<Task> tasks = new LinkedList<Task>();

    @Override
    public void addTask(Task task) throws RemoteException {
        tasks.add(task);
    }

    @Override
    public Task nextTask() throws RemoteException {
        return (Task) tasks.remove();
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

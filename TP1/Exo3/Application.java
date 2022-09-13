import java.rmi.RemoteException;
import java.util.HashMap;

public class Application {
    public class BagOfResultImp extends Remote implements BagOfResult{
        HashMap<Integer, Boolean> bagOfResult;
        
        public BagOfResultImp(){
            this.bagOfResult = new HashMap<>();
        }

        @Override
        public void addResult(int primeNumber, boolean result) throws java.rmi.RemoteException {
            this.bagOfResult.put(primeNumber, result);
        }

    }

    public class BagOfTaskImp extends Remote implements BagOfTask {

        int currentTask = 0;
        @Override
        public Task nextTask() throws java.rmi.RemoteException {
        }
        
        @Override
        public void sendResult(Task task) throws java.rmi.RemoteException {
        }
    }
}

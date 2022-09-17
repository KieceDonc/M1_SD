import java.util.HashMap;

public class Server {
    public class BagOfResultImp extends Remote implements BagOfResult {
        HashMap<Integer, Boolean> bagOfResult;

        public BagOfResultImp() {
            this.bagOfResult = new HashMap<>();
        }

        @Override
        public void addResult(int primeNumber, boolean result) throws java.rmi.RemoteException {
            this.bagOfResult.put(primeNumber, result);
        }

    }
}

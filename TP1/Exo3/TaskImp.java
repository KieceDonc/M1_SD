import java.io.Serializable;
import java.rmi.RemoteException;

public class TaskImp implements Task, Serializable {
    BagOfResult bagOfResult;

    int primeNumber;
    boolean isPrimeNumber = true;

    public TaskImp(BagOfResult bagOfResult, int primeNumber) {
        this.bagOfResult = bagOfResult;
        this.primeNumber = primeNumber;
    }

    @Override
    public void execute() {
        // primeNumber = 2 ?
        if (this.primeNumber <= 1) {
            this.isPrimeNumber = false;
        } else {
            boolean shouldContinue = true;
            int index = 2;
            do {
                if (this.primeNumber % index == 0) {
                    shouldContinue = false;
                    this.isPrimeNumber = false;
                }
                index++;
                if (index == this.primeNumber) {
                    shouldContinue = false;
                }
            } while (shouldContinue);

            try {
                bagOfResult.addResult(this.primeNumber, this.isPrimeNumber);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
}
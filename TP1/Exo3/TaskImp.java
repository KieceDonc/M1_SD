import java.io.Serializable;

public class TaskImp implements Task, Serializable {
    BagOfResult bagOfResult;

    int primeNumber;
    boolean isPrimeNumber = true; 

    public Task(BagOfResult bagOfResult, int primeNumber){
        this.bagOfResult = bagOfResult;
        this.primeNumber = primeNumber;
    }

    @Override
    public void execute() {
        // primeNumber = 2 ?
        if(this.primeNumber =< 1){
            this.primeNumber = false;
        }else{
            boolean shouldContinue = true;
            int index = 2;
            do{
                if(this.primeNumber % index == 0){
                    shouldContinue = false;
                    this.isPrimeNumber = false;
                }
                index++;
                if(index == this.primeNumber){
                    shouldContinue = false;
                }
            }while(shouldContinue);

            bagOfResult.addResult(this.primeNumber, this.isPrimeNumber);
        }
    }
}        
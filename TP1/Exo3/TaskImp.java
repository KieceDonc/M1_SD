
public class TaskImp implements Task {

    private int number;
    private boolean isPrimeNumber = true;

    public TaskImp(int number) {
        this.number = number;
    }

    @Override
    public void execute() {
        // number = 2 ?
        if (this.number <= 1) {
            this.isPrimeNumber = false;
        } else {
            boolean shouldContinue = true;
            int index = 2;
            do {
                if (this.number % index == 0) {
                    shouldContinue = false;
                    this.isPrimeNumber = false;
                }
                index++;
                if (index == this.number) {
                    shouldContinue = false;
                }
            } while (shouldContinue);
        }
    }

    @Override
    public int getNumber() {
        return this.number;
    }

    @Override
    public boolean isPrimeNumber() {
        return this.isPrimeNumber;
    }
}
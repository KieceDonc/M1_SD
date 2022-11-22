public class ClientActor {

    private LoggingAdapter log = Logging.getLogger(this.getContext().system(), this);

    public class GetBanker {

    }

    public class GetBalance {

    }

    public class Withdraw extends BankOperation {

        public Withdraw(double amount) {
            super(amount);
        }
    }

    public class Deposit extends BankOperation {

        public Deposit(double amount) {
            super(amount);
        }
    }

    private class BankOperation {

        private double amount;

        public BankOperation(double amount) {
            this.amount = amount;
        }

        public double getAmount() {
            return this.amount;
        }
    }
}

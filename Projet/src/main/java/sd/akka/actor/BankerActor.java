public class BankerActor {

    private LoggingAdapter log = Logging.getLogger(this.getContext().system(), this);

    public class GetBalance {

        private int clientID;

        public GetBalance(int clientID) {
            this.clientID = clientID;
        }

        public int getClientID(){
            this.clientID;
        }
    }

    public class Withdraw extends BankOperation {

        public Withdraw(int clientID, double amount) {
            super(clientID, amount);
        }
    }

    public class Deposit extends BankOperation {

        public Deposit(int clientID, double amount) {
            super(clientID, amount);
        }
    }

    private class BankOperation {

        private int clientID;
        private double amount;

        public BankOperation(int clientID, double amount) {
            this.clientID = clientID;
            this.amount = amount;
        }

        public int getClientID() {
            return this.clientID;
        }

        public double getAmount() {
            return this.amount;
        }
    }

}

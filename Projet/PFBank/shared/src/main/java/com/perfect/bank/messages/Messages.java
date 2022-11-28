package com.perfect.bank.messages;

import java.io.Serializable;

public class Messages {

    public interface Message extends Serializable {
    }

    public static class GetClientUID implements Message {
        public GetClientUID() {
        }
    }

    public static class GetBankerUID implements Message {
        public GetBankerUID() {
        }
    }

    public class Withdraw extends BankOperation {

        public Withdraw(int clientUID, double amount) {
            super(clientUID, amount);
        }
    }

    public class Deposit extends BankOperation {

        public Deposit(int clientUID, double amount) {
            super(clientUID, amount);
        }
    }

    private class BankOperation extends ClientInfo implements Message {

        private double amount;

        public BankOperation(int clientUID, double amount) {
            super(clientUID);
            this.amount = amount;
        }

        public double getAmount() {
            return this.amount;
        }
    }

    public class GetBalance extends ClientInfo implements Message {

        public GetBalance(int clientUID) {
            super(clientUID);
        }
    }

    private class ClientInfo {

        private int clientUID;

        public ClientInfo(int clientUID) {
            this.clientUID = clientUID;
        }

        public int getClientUID() {
            return this.clientUID;
        }
    }
}

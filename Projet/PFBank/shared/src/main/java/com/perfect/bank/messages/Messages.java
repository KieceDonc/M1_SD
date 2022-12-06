package com.perfect.bank.messages;

import java.io.Serializable;

public class Messages {

    public interface Message extends Serializable {

    }

    public static class IsClientExist extends ClientInfo {

        public IsClientExist(int clientUID) {
            super(clientUID);
        }
    }

    public static class CreateClientUID implements Message {

        public CreateClientUID() {
        }
    }

    public static class SetClientUID extends ClientInfo {

        public SetClientUID(int clientUID) {
            super(clientUID);
        }
    }

    public static class DeclareAsBanker implements Message {
        public DeclareAsBanker() {
        }
    }

    public static class UnvalidateBanker implements Message {
        public UnvalidateBanker() {

        }
    }

    public static class Withdraw extends BankOperation {

        public Withdraw(int clientUID, double amount) {
            super(clientUID, amount);
        }
    }

    public static class Deposit extends BankOperation {

        public Deposit(int clientUID, double amount) {
            super(clientUID, amount);
        }
    }

    private static class BankOperation extends ClientInfo {

        private double amount;

        public BankOperation(int clientUID, double amount) {
            super(clientUID);
            this.amount = amount;
        }

        public double getAmount() {
            return this.amount;
        }
    }

    public static class GetBalance extends ClientInfo {

        public GetBalance(int clientUID) {
            super(clientUID);
        }
    }

    public static class GetBalanceResponse implements Message {

        private double balance;

        public GetBalanceResponse(double balance) {
            this.balance = balance;
        }

        public double getBalance() {
            return this.balance;
        }
    }

    private static class ClientInfo implements Message {

        private int clientUID;

        public ClientInfo(int clientUID) {
            this.clientUID = clientUID;
        }

        public int getClientUID() {
            return this.clientUID;
        }
    }

    public static class Shutdown implements Message {

        public Shutdown() {

        }
    }
}

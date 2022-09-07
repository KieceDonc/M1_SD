package TP1.Exo2;

import java.rmi.Remote;

public interface AccountMethods extends Remote {
    public int getBalance(int id) throws java.rmi.RemoteException;

    public void deposit(int id, int amount) throws java.rmi.RemoteException;

    public void withdraw(int id, int amount) throws java.rmi.RemoteException;
}
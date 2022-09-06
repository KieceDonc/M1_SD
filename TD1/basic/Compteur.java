import java.rmi.*;

public interface Compteur extends Remote {
    public int Somme(int a, int b) throws java.rmi.RemoteException;
}
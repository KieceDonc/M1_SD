import java.rmi.server.*;
import java.net.*;
import java.rmi.*;

public class ImpServeurHello extends UnicastRemoteObject implements Hello{
    public ImpServeurHello () throws RemoteException{super ();}

    public String ditBonjour () throws RemoteException{return "bonjour a tous";}


    public static void main(String arg []){
        try{
        ImpServeurHello s=new ImpServeurHello ();
        String nom="nomdelobjet";
        Naming.rebind(nom ,s); // enregistrement
        System.out.println("Serveur enregistré");

        Naming.rebind("addition", s);

        LocateRegistry.createRegistry(6600);
        // Register communication route
        Naming.rebind("rmi://0.0.0.0:6600/PersonService", personService);
        
        LocateRegistry.getRegistry("172.31.18.40", 6600);
        
        }
        catch (Exception e){System.err.println("Erreur :"+e);}
    }
}

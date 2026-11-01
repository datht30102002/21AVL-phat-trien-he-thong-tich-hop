package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface PrimeService extends Remote {
    boolean isPrime(int number) throws RemoteException;
}

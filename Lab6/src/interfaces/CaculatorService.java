package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface CaculatorService extends Remote {
    int add(int a, int b) throws RemoteException;
}

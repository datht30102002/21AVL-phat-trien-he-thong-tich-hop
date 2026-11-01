package services;

import interfaces.CaculatorService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class CaculatorServiceImpl extends UnicastRemoteObject implements CaculatorService {
    public CaculatorServiceImpl() throws RemoteException{
        super();
    }
    @Override
    public int add(int a, int b) throws RemoteException {
        return a + b;
    }
}

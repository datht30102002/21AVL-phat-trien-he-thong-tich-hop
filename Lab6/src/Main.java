import interfaces.CaculatorService;
import services.CaculatorServiceImpl;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Main {
    public static void main(String[] args){
        try{
            Registry registry = LocateRegistry.createRegistry(1100);
            CaculatorService service = new CaculatorServiceImpl();
            registry.rebind("caculator", service);
            System.out.println("RMI Server (CalculatorService) đang chạy...");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}

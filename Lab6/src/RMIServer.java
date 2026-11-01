import interfaces.*;
import services.*;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIServer {
    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.createRegistry(1099);

            HelloService Helloservice = new HelloServiceImpl();
            CaculatorService Caculatorserice = new CaculatorServiceImpl();
            PrimeService Primeservice = new PrimeServiceImpl();
            ContactService Contactservice = new ContactServiceImpl();
            ChatService Chatservice = new ChatServiceImpl();
            BankService BankService = new BankServiceImpl(1000000);
            GeometryService Geometryservice = new GeometryServiceImpl();
            FlightBookingService FlightBookingservice = new FlightBookingServiceImpl();
            AuctionService Auctionservice = new AuctionServiceImpl();

            registry.rebind("interfaces.HelloService", Helloservice);
            registry.rebind("interfaces.CaculatorService", Caculatorserice);
            registry.rebind("interfaces.PrimeService", Primeservice);
            registry.rebind("interfaces.ContactService", Contactservice);
            registry.rebind("interfaces.ChatService", Chatservice);
            registry.rebind("interfaces.BankService", BankService);
            registry.rebind("interfaces.GeometryService", Geometryservice);
            registry.rebind("interfaces.FlightBookingService", FlightBookingservice);
            registry.rebind("interfaces.AuctionService", Auctionservice);

            System.out.println("RMI Server đang chạy...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
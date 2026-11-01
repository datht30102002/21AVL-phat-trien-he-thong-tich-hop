package interfaces;

import models.Product;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface AuctionService extends Remote {
    // Cho Seller
    boolean addProduct(String id, String name, double initialPrice) throws RemoteException;

    // Cho Bidder
    List<Product> getAllProducts() throws RemoteException;
    boolean placeBid(String productId, double bidAmount, String bidderName) throws RemoteException;
}
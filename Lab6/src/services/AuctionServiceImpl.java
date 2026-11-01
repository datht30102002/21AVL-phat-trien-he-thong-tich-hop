package services;

import interfaces.AuctionService;
import models.Product;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AuctionServiceImpl extends UnicastRemoteObject implements AuctionService {
    private final Map<String, Product> productMap;

    public AuctionServiceImpl() throws RemoteException {
        super();
        this.productMap = new ConcurrentHashMap<>();
    }

    @Override
    public synchronized boolean addProduct(String id, String name, double initialPrice) throws RemoteException {
        if (productMap.containsKey(id) || initialPrice <= 0) {
            return false;
        }
        Product product = new Product(id, name, initialPrice);
        productMap.put(id, product);
        System.out.printf("\n[SERVER-NOTIFY]: Sản phẩm mới được thêm: %s (Giá khởi điểm: %,.2f VNĐ)\n", name, initialPrice);
        return true;
    }

    @Override
    public synchronized List<Product> getAllProducts() throws RemoteException {
        return new ArrayList<>(productMap.values());
    }

    @Override
    public synchronized boolean placeBid(String productId, double bidAmount, String bidderName) throws RemoteException {
        Product product = productMap.get(productId);
        if (product != null && bidAmount > product.getCurrentPrice()) {
            product.setCurrentPrice(bidAmount);
            product.setHighestBidder(bidderName);

            // Thông báo trên Server Console khi có người ra giá cao hơn
            System.out.printf("\n[SERVER-NOTIFY]: %s vừa ra giá %,.2f VNĐ cho sản phẩm '%s' (Mã: %s)\n",
                    bidderName, bidAmount, product.getName(), productId);
            return true;
        }
        return false;
    }
}
package models;

import java.io.Serializable;

public class Product implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private double currentPrice;
    private String highestBidder;

    public Product(String id, String name, double initialPrice) {
        this.id = id;
        this.name = name;
        this.currentPrice = initialPrice;
        this.highestBidder = "Chưa có";
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public double getCurrentPrice() { return currentPrice; }
    public void setCurrentPrice(double currentPrice) { this.currentPrice = currentPrice; }
    public String getHighestBidder() { return highestBidder; }
    public void setHighestBidder(String highestBidder) { this.highestBidder = highestBidder; }

    @Override
    public String toString() {
        return String.format("Mã SP: %s | Tên: %s | Giá hiện tại: %,.2f VNĐ | Người đặt cao nhất: %s",
                id, name, currentPrice, highestBidder);
    }
}
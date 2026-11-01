package services;

import interfaces.FlightBookingService;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FlightBookingServiceImpl extends UnicastRemoteObject implements FlightBookingService {
    // Quản lý số ghế của các chuyến bay (Thread-safe)
    private final Map<String, Integer> flightMap;

    public FlightBookingServiceImpl() throws RemoteException {
        super();
        flightMap = new ConcurrentHashMap<>();
        // Khởi tạo sẵn một số chuyến bay mẫu
        flightMap.put("VN123", 100);
        flightMap.put("VJ456", 50);
        flightMap.put("QH789", 20);
    }

    @Override
    public synchronized boolean bookTicket(String flightNumber, int seats) throws RemoteException {
        if (!flightMap.containsKey(flightNumber) || seats <= 0) {
            return false;
        }
        int currentSeats = flightMap.get(flightNumber);
        if (currentSeats >= seats) {
            flightMap.put(flightNumber, currentSeats - seats);
            return true;
        }
        return false;
    }

    @Override
    public synchronized int availableSeats(String flightNumber) throws RemoteException {
        return flightMap.getOrDefault(flightNumber, -1); // -1 nếu không tìm thấy chuyến bay
    }

    @Override
    public synchronized boolean cancelBooking(String flightNumber, int seats) throws RemoteException {
        if (!flightMap.containsKey(flightNumber) || seats <= 0) {
            return false;
        }
        int currentSeats = flightMap.get(flightNumber);
        flightMap.put(flightNumber, currentSeats + seats);
        return true;
    }
}
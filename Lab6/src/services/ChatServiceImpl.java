package services;

import interfaces.ChatService;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ChatServiceImpl extends UnicastRemoteObject implements ChatService {
    private final BufferedReader reader;

    public ChatServiceImpl() throws RemoteException {
        super();
        this.reader = new BufferedReader(new InputStreamReader(System.in));
    }

    @Override
    public synchronized String sendMessage(String message) throws RemoteException {
        try {
            System.out.println("\n[Client]: " + message);
            System.out.print("[Server trả lời]: ");

            String response = reader.readLine();
            return (response != null) ? response.trim() : "";
        } catch (IOException e) {
            throw new RemoteException("Lỗi đọc dữ liệu từ Server", e);
        }
    }
}
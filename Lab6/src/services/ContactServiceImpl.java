package services;

import interfaces.ContactService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ContactServiceImpl extends UnicastRemoteObject implements ContactService {
    private final Map<String, String> contacts;
    public ContactServiceImpl() throws RemoteException {
        super();
        contacts = new ConcurrentHashMap<>();
    }

    @Override
    public void addContact(String name, String phone) throws RemoteException {
        contacts.put(name.trim(), phone.trim());
        System.out.println("Đã thêm/cập nhật liên hệ: " + name + " - " + phone);
    }

    @Override
    public String findContact(String name) throws RemoteException {
        return contacts.get(name.trim());    }

    @Override
    public boolean deleteContact(String name) throws RemoteException {
        if (contacts.containsKey(name.trim())) {
            contacts.remove(name.trim());
            System.out.println("Đã xóa liên hệ: " + name);
            return true;
        }
        return false;
    }
}

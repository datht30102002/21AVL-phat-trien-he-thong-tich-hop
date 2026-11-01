package services;

import interfaces.BankService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class BankServiceImpl extends UnicastRemoteObject implements BankService {
    private double balance;
    public BankServiceImpl(double initialBalance) throws RemoteException{
        super();
        this.balance = initialBalance;
    }

    @Override
    public double getBalance() throws RemoteException {
        return this.balance;
    }

    @Override
    public void deposit(double amount) throws RemoteException {
        if(amount > 0){
            this.balance += amount;
            System.out.println("Nạp tiền: +" + amount + " | Số dư mới: " +this.balance);
        }
    }

    @Override
    public boolean withdraw(double amount) throws RemoteException {
        if(amount > 0 && this.balance >= amount){
            this.balance -= amount;
            System.out.println("Rút tiền -" + amount + " | Số dư mới: " +this.balance);
            return true;
        }
        return false;
    }
}

import java.util.ArrayList;

// Account class to manage balance, PIN, and transaction history
// Includes methods for deposit, withdrawal, and transaction logging
// Constructor initializes balance, PIN, and empty transaction history


public class Account {
    private final String name;
    private double balance;
    private int pin;
    private final ArrayList<String> history;

    public Account(String name, double initialBalance, int pin) {
        this.name = name;
        this.balance = initialBalance;
        this.pin = pin;
        this.history = new ArrayList<>();
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }


    public String getName() {
        return name;
    }

    public String showBalance() {
        return String.format(" Your Current Balance is: PHP %.2f", balance);
    }

    public double getBalance() {
        return balance;
    }

    public int getPin() {
        return pin;
    }

    public void setPin(int newPin) {    
        this.pin = newPin;
    }

    public ArrayList<String> getTransactionHistory() {
        return new ArrayList<>(history);
    }

    public void deposit(double amount) {
        balance += amount;
    }

    public boolean withdraw(double amount) {
        if (amount <= balance) {
            balance -= amount;
            return true;
        }
        return false;
    }

    public void addTransaction(String record) {
        history.add(record);
    }
}
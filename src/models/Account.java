package models;

public class Account {
    private String accountNumber;
    private double balance;
    private User owner;
    private String accountType;

    public Account(String accountNumber, double balance, User owner, String accountType) {
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.owner = owner;
        this.accountType = accountType;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    @Override
    public String toString() {
        return "Account{" + "accountNumber='" + accountNumber + '\'' + ", balance=" + balance + ", owner=" + owner.getFirstName() + " " + owner.getLastName() + ", accountType='" + accountType + '\'' + '}';
    }
}
package models;

import java.time.LocalDateTime;

public class Transaction {
    private String transactionId;
    private double amount;
    private String type;
    private LocalDateTime date;
    private Account accountFrom;
    private Account accountTo;

    public Transaction(String transactionId, double amount, String type, LocalDateTime date, Account accountFrom, Account accountTo) {
        this.transactionId = transactionId;
        this.amount = amount;
        this.type = type;
        this.date = date;
        this.accountFrom = accountFrom;
        this.accountTo = accountTo;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Account getAccountFrom() {
        return accountFrom;
    }

    public void setAccountFrom(Account accountFrom) {
        this.accountFrom = accountFrom;
    }

    public Account getAccountTo() {
        return accountTo;
    }

    public void setAccountTo(Account accountTo) {
        this.accountTo = accountTo;
    }

    @Override
    public String toString() {
        return "Transaction [ID=" + transactionId + ", Amount=" + amount + ", Type=" + type + ", Date=" + date + ", From Account=" + accountFrom.getAccountNumber() + ", To Account=" + accountTo.getAccountNumber() + "]";
    }
}
package services;

import dal.DALUser;
import dal.DALAccount;
import dal.DALTransaction;
import models.User;
import models.Account;
import models.Role;
import models.Transaction;

import java.time.LocalDateTime;
import java.util.List;

public class BanqueSystem {

    private DALUser dalUser = new DALUser();
    private DALAccount dalAccount = new DALAccount();
    private DALTransaction dalTransaction = new DALTransaction();

    public User login(String email, String password) {
        return dalUser.getUserByEmail(email);
    }

    public void createUser(String firstName, String lastName, String email, String password, Role role) {
        User user = new User(0, firstName, lastName, password, email, role); 
        dalUser.addUser(user);
    }

    public void createAccount(String accountNumber, double initialBalance, User owner, String accountType) {
        Account account = new Account(accountNumber, initialBalance, owner, accountType);
        dalAccount.addAccount(account);
    }

    public void deposit(String accountNumber, double amount) {
        Account account = dalAccount.getAccountByNumber(accountNumber);
        if (account != null) {
            account.setBalance(account.getBalance() + amount);
            dalAccount.updateAccount(account);

            Transaction transaction = new Transaction(
                "TXN" + System.currentTimeMillis(), 
                amount,
                "DEPOSIT",
                LocalDateTime.now(),
                null, 
                account
            );
            dalTransaction.addTransaction(transaction);
        }
    }

    public void withdraw(String accountNumber, double amount) {
        Account account = dalAccount.getAccountByNumber(accountNumber);
        if (account != null && account.getBalance() >= amount) {
            account.setBalance(account.getBalance() - amount);
            dalAccount.updateAccount(account);

            Transaction transaction = new Transaction(
                "TXN" + System.currentTimeMillis(), 
                amount,
                "WITHDRAWAL",
                LocalDateTime.now(),
                account,
                null 
            );
            dalTransaction.addTransaction(transaction);
        }
    }

    public void transfer(String fromAccountNumber, String toAccountNumber, double amount) {
        Account fromAccount = dalAccount.getAccountByNumber(fromAccountNumber);
        Account toAccount = dalAccount.getAccountByNumber(toAccountNumber);

        if (fromAccount != null && toAccount != null && fromAccount.getBalance() >= amount) {
            fromAccount.setBalance(fromAccount.getBalance() - amount);
            toAccount.setBalance(toAccount.getBalance() + amount);

            dalAccount.updateAccount(fromAccount);
            dalAccount.updateAccount(toAccount);

            Transaction transaction = new Transaction(
                "TXN" + System.currentTimeMillis(), 
                amount,
                "TRANSFER",
                LocalDateTime.now(),
                fromAccount,
                toAccount
            );
            dalTransaction.addTransaction(transaction);
        }
    }

    public List<Transaction> getAccountTransactions(String accountNumber) {
        return dalTransaction.getTransactionsByAccount(accountNumber);
    }

    public List<Account> getUserAccounts(int userId) {
        return dalAccount.getAccountsByUserId(userId);
    }
}
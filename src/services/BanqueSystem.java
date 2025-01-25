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

    public BanqueSystem() {
        initializeDefaultAdmin();
    }

    private void initializeDefaultAdmin() {
        User admin = dalUser.getUserByEmail("admin@example.com");
        if (admin == null) {
            createUser("Admin", "User", "admin@example.com", "admin123", Role.ADMIN);
            System.out.println("Admin par défaut créé : admin@example.com / admin123");
        }
    }

    public User login(String email, String password) {
        User user = dalUser.getUserByEmail(email);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null; 
    }

    public void createUser(String firstName, String lastName, String email, String password, Role role) {
        User user = new User(0, firstName, lastName, password, email, role);
        dalUser.addUser(user);
    }

    public void createAccount(String accountNumber, double initialBalance, User owner, String accountType) {
        Account account = new Account(accountNumber, initialBalance, owner, accountType);
        dalAccount.addAccount(account);
    }

    public boolean deposit(String accountNumber, double amount) {
        if (amount <= 0) {
            System.out.println("Amount must be positive.");
            return false;
        }

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
            return true;
        }
        System.out.println("Account not found.");
        return false;
    }

    public boolean withdraw(String accountNumber, double amount) {
        if (amount <= 0) {
            System.out.println("Amount must be positive.");
            return false;
        }

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
            return true;
        }
        System.out.println("Withdrawal failed. Insufficient balance or account not found.");
        return false;
    }

public boolean transfer(String fromAccountNumber, String toAccountNumber, double amount, int userId) {
    if (amount <= 0) {
        System.out.println("Amount must be positive.");
        return false;
    }

    Account fromAccount = dalAccount.getAccountByNumber(fromAccountNumber);
    Account toAccount = dalAccount.getAccountByNumber(toAccountNumber);

    if (fromAccount == null || fromAccount.getOwner().getId() != userId) {
        System.out.println("Transfer failed. You do not own the source account.");
        return false;
    }

    if (toAccount == null) {
        System.out.println("Transfer failed. Destination account not found.");
        return false;
    }

    if (fromAccount.getBalance() < amount) {
        System.out.println("Transfer failed. Insufficient balance.");
        return false;
    }

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

    System.out.println("Transfer successful.");
    return true;
}


    public List<Transaction> getAccountTransactions(String accountNumber) {
        return dalTransaction.getTransactionsByAccount(accountNumber);
    }

    public List<Account> getUserAccounts(int userId) {
        return dalAccount.getAccountsByUserId(userId);
    }

    public User getUserById(int userId) {
        return dalUser.getUserById(userId);
    }

    public Account getAccountByNumber(String accountNumber) {
        return dalAccount.getAccountByNumber(accountNumber);
    }
}



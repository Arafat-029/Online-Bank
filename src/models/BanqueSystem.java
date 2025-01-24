package models;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.time.LocalDateTime;

public class BanqueSystem {
    private List<User> users; 
    private List<Account> accounts; 
    private List<Transaction> transactions; 
    private User currentUser; 

    public BanqueSystem() {
        this.users = new ArrayList<>();
        this.accounts = new ArrayList<>();
        this.transactions = new ArrayList<>();
        initializeAdmin(); 
    }

    private void initializeAdmin() {
        User admin = new User(1, "Admin", "System", "admin123", "admin@banque.com", Role.ADMIN);
        users.add(admin);
    }

    public boolean login(String email, String password) {
        for (User user : users) {
            if (user.getEmail().equals(email) && user.getPassword().equals(password)) {
                currentUser = user;
                return true;
            }
        }
        return false;
    }

    public void logout() {
        currentUser = null;
    }

    public boolean createAccount(String accountNumber, double initialBalance, User owner, String accountType) {
        if (currentUser != null && currentUser.getRole() == Role.ADMIN) {
            Account account = new Account(accountNumber, initialBalance, owner, accountType);
            accounts.add(account);
            owner.getAccounts().add(account); 
            return true;
        }
        return false;
    }

    public boolean deleteAccount(String accountNumber) {
        if (currentUser != null && currentUser.getRole() == Role.ADMIN) {
            Account account = findAccountByNumber(accountNumber);
            if (account != null) {
                accounts.remove(account);
                account.getOwner().getAccounts().remove(account); 
                return true;
            }
        }
        return false;
    }

    public boolean deposit(String accountNumber, double amount) {
        if (currentUser != null) {
            Account account = findAccountByNumber(accountNumber);
            if (account != null && account.getOwner().equals(currentUser)) {
                account.setBalance(account.getBalance() + amount);
                Transaction transaction = new Transaction(
                    "TXN" + (transactions.size() + 1),
                    amount,
                    "Dépôt",
                    LocalDateTime.now(),
                    null,
                    account
                );
                transactions.add(transaction);
                return true;
            }
        }
        return false;
    }

    public boolean withdraw(String accountNumber, double amount) {
        if (currentUser != null) {
            Account account = findAccountByNumber(accountNumber);
            if (account != null && account.getOwner().equals(currentUser) && account.getBalance() >= amount) {
                account.setBalance(account.getBalance() - amount);
                Transaction transaction = new Transaction(
                    "TXN" + (transactions.size() + 1),
                    amount,
                    "Retrait",
                    LocalDateTime.now(),
                    account,
                    null
                );
                transactions.add(transaction);
                return true;
            }
        }
        return false;
    }

    public boolean transfer(String fromAccountNumber, String toAccountNumber, double amount) {
        if (currentUser != null) {
            Account fromAccount = findAccountByNumber(fromAccountNumber);
            Account toAccount = findAccountByNumber(toAccountNumber);
            if (fromAccount != null && toAccount != null && fromAccount.getOwner().equals(currentUser) && fromAccount.getBalance() >= amount) {
                fromAccount.setBalance(fromAccount.getBalance() - amount);
                toAccount.setBalance(toAccount.getBalance() + amount);
                Transaction transaction = new Transaction(
                    "TXN" + (transactions.size() + 1),
                    amount,
                    "Transfert",
                    LocalDateTime.now(),
                    fromAccount,
                    toAccount
                );
                transactions.add(transaction);
                return true;
            }
        }
        return false;
    }

    public List<Account> getUserAccounts() {
        if (currentUser != null) {
            return currentUser.getAccounts();
        }
        return new ArrayList<>();
    }

    public List<Transaction> getUserTransactions() {
        if (currentUser != null) {
            return transactions.stream()
                .filter(t -> t.getAccountFrom() != null && t.getAccountFrom().getOwner().equals(currentUser) ||
                             t.getAccountTo() != null && t.getAccountTo().getOwner().equals(currentUser))
                .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    public List<User> getAllUsers() {
        if (currentUser != null && currentUser.getRole() == Role.ADMIN) {
            return users;
        }
        return new ArrayList<>();
    }

    public List<Transaction> getAllTransactions() {
        if (currentUser != null && currentUser.getRole() == Role.ADMIN) {
            return transactions;
        }
        return new ArrayList<>();
    }

    private Account findAccountByNumber(String accountNumber) {
        return accounts.stream()
            .filter(account -> account.getAccountNumber().equals(accountNumber))
            .findFirst()
            .orElse(null);
    }
}





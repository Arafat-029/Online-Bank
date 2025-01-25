package presentation;

import services.BanqueSystem;
import models.User;
import models.Role;
import models.Account;
import models.Transaction;

import java.util.List;
import java.util.Scanner;

public class BankAppMenus {
    private Scanner scanner;
    private BanqueSystem banqueSystem;
    private User currentUser; 
    public BankAppMenus(Scanner scanner, BanqueSystem banqueSystem) {
        this.scanner = scanner;
        this.banqueSystem = banqueSystem;
    }

    public void showLoginMenu() {
        while (true) {
            System.out.println("=== Login ===");
            System.out.println("1. Log in");
            System.out.println("2. Exit application");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    System.out.print("Enter your email: ");
                    String email = scanner.next();
                    System.out.print("Enter your password: ");
                    String password = scanner.next();

                    User user = banqueSystem.login(email, password);
                    if (user != null) {
                        currentUser = user; 
                        System.out.println("Login successful. Welcome, " + user.getFirstName() + "!");
                        if (user.getRole() == Role.ADMIN) {
                            showAdminMenu();
                        } else {
                            showClientMenu(user);
                        }
                    } else {
                        System.out.println("Login failed. Invalid email or password.");
                    }
                    break;

                case 2:
                    System.out.println("Thank you for using our application. Goodbye!");
                    System.exit(0);

                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void showAdminMenu() {
        while (true) {
            System.out.println("=== Admin Menu ===");
            System.out.println("1. Manage users");
            System.out.println("2. Manage accounts");
            System.out.println("3. Logout");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    showManageUsersMenu();
                    break;
                case 2:
                    showManageAccountsMenu();
                    break;
                case 3:
                    logout();
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void showClientMenu(User user) {
        while (true) {
            System.out.println("=== Client Menu ===");
            System.out.println("1. View my accounts");
            System.out.println("2. Perform a transaction");
            System.out.println("3. View my transactions");
            System.out.println("4. Logout");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    List<Account> accounts = banqueSystem.getUserAccounts(user.getId());
                    if (accounts.isEmpty()) {
                        System.out.println("No accounts found.");
                    } else {
                        for (Account account : accounts) {
                            System.out.println(account);
                        }
                    }
                    break;
                case 2:
                    showPerformTransactionMenu(user);
                    break;
                case 3:
                    System.out.print("Enter your account number: ");
                    String accountNumber = scanner.next();
                    List<Transaction> transactions = banqueSystem.getAccountTransactions(accountNumber);
                    if (transactions.isEmpty()) {
                        System.out.println("No transactions found.");
                    } else {
                        for (Transaction transaction : transactions) {
                            System.out.println(transaction);
                        }
                    }
                    break;
                case 4:
                    logout();
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void showManageUsersMenu() {
        while (true) {
            System.out.println("=== Manage Users ===");
            System.out.println("1. Add a user");
            System.out.println("2. Back to main menu");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    System.out.print("Enter first name: ");
                    String firstName = scanner.next();
                    System.out.print("Enter last name: ");
                    String lastName = scanner.next();
                    System.out.print("Enter email: ");
                    String email = scanner.next();
                    System.out.print("Enter password: ");
                    String password = scanner.next();
                    System.out.print("Enter role (ADMIN/CLIENT): ");
                    String roleStr = scanner.next();

                    try {
                        Role role = Role.valueOf(roleStr.toUpperCase());
                        banqueSystem.createUser(firstName, lastName, email, password, role);
                        System.out.println("User created successfully.");
                    } catch (IllegalArgumentException e) {
                        System.out.println("Invalid role. Please enter ADMIN or CLIENT.");
                    }
                    break;
                case 2:
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void showManageAccountsMenu() {
        while (true) {
            System.out.println("=== Manage Accounts ===");
            System.out.println("1. Create an account");
            System.out.println("2. Back to main menu");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    System.out.print("Enter account number: ");
                    String accountNumber = scanner.next();
                    System.out.print("Enter initial balance: ");
                    double initialBalance = scanner.nextDouble();
                    System.out.print("Enter user ID: ");
                    int userId = scanner.nextInt();

                    User owner = banqueSystem.getUserById(userId);
                    if (owner == null) {
                        System.out.println("User not found.");
                        continue;
                    }

                    System.out.print("Enter account type: ");
                    String accountType = scanner.next();
                    banqueSystem.createAccount(accountNumber, initialBalance, owner, accountType);
                    System.out.println("Account created successfully.");
                    break;
                case 2:
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void showPerformTransactionMenu(User user) {
        while (true) {
            System.out.println("=== Perform a Transaction ===");
            System.out.println("1. Deposit money");
            System.out.println("2. Withdraw money");
            System.out.println("3. Transfer money");
            System.out.println("4. Back to main menu");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    System.out.print("Enter account number: ");
                    String accountNumber = scanner.next();
                    System.out.print("Enter amount: ");
                    double amount = scanner.nextDouble();
                    if (banqueSystem.deposit(accountNumber, amount)) {
                        System.out.println("Deposit successful.");
                    }
                    break;
                case 2:
                    System.out.print("Enter account number: ");
                    accountNumber = scanner.next();
                    System.out.print("Enter amount: ");
                    amount = scanner.nextDouble();
                    if (banqueSystem.withdraw(accountNumber, amount)) {
                        System.out.println("Withdrawal successful.");
                    }
                    break;
                case 3:
                    if (currentUser == null) {
                        System.out.println("You must be logged in to perform a transfer.");
                        break;
                    }

                    System.out.print("Enter source account number: ");
                    String fromAccount = scanner.next();
                    System.out.print("Enter destination account number: ");
                    String toAccount = scanner.next();
                    System.out.print("Enter amount: ");
                    amount = scanner.nextDouble();

                    int userId = currentUser.getId();
                    if (banqueSystem.transfer(fromAccount, toAccount, amount, userId)) {
                        System.out.println("Transfer successful.");
                    } else {
                        System.out.println("Transfer failed. Please check the details and try again.");
                    }
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void logout() {
        if (currentUser != null) {
            System.out.println("Goodbye, " + currentUser.getFirstName() + "!");
            currentUser = null;
        } else {
            System.out.println("No user is currently logged in.");
        }
    }
}

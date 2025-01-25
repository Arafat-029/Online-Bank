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
            scanner.nextLine(); 

            switch (choice) {
                case 1:
                    System.out.print("Enter your email: ");
                    String email = scanner.nextLine();
                    System.out.print("Enter your password: ");
                    String password = scanner.nextLine();

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
            scanner.nextLine(); 

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
            System.out.println("4. Change password");
            System.out.println("5. Logout");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); 

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
                    String accountNumber = scanner.nextLine();
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
                    showChangePasswordMenu();
                    break;
                case 5:
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
            System.out.println("2. Edit user information");
            System.out.println("3. Delete user");
            System.out.println("4. Back to main menu");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); 

            switch (choice) {
                case 1:
                    System.out.print("Enter first name: ");
                    String firstName = scanner.nextLine();
                    System.out.print("Enter last name: ");
                    String lastName = scanner.nextLine();
                    System.out.print("Enter email: ");
                    String email = scanner.nextLine();
                    System.out.print("Enter password: ");
                    String password = scanner.nextLine();
                    System.out.print("Enter role (ADMIN/CLIENT): ");
                    String roleStr = scanner.nextLine();

                    try {
                        Role role = Role.valueOf(roleStr.toUpperCase());
                        banqueSystem.createUser(firstName, lastName, email, password, role);
                        System.out.println("User created successfully.");
                    } catch (IllegalArgumentException e) {
                        System.out.println("Invalid role. Please enter ADMIN or CLIENT.");
                    }
                    break;
                case 2:
                    showEditUserMenu();
                    break;
                case 3:
                    showDeleteUserMenu();
                    break;
                case 4:
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
            System.out.println("2. Edit account information");
            System.out.println("3. Back to main menu");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); 

            switch (choice) {
                case 1:
                    System.out.print("Enter account number: ");
                    String accountNumber = scanner.nextLine();
                    System.out.print("Enter initial balance: ");
                    double initialBalance = scanner.nextDouble();
                    scanner.nextLine(); 
                    System.out.print("Enter user ID: ");
                    int userId = scanner.nextInt();
                    scanner.nextLine(); 

                    User owner = banqueSystem.getUserById(userId);
                    if (owner == null) {
                        System.out.println("User not found.");
                        continue;
                    }

                    System.out.print("Enter account type: ");
                    String accountType = scanner.nextLine();
                    banqueSystem.createAccount(accountNumber, initialBalance, owner, accountType);
                    System.out.println("Account created successfully.");
                    break;
                case 2:
                    showEditAccountMenu();
                    break;
                case 3:
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
            scanner.nextLine(); 

            switch (choice) {
                case 1:
                    System.out.print("Enter account number: ");
                    String accountNumber = scanner.nextLine();
                    System.out.print("Enter amount: ");
                    double amount = scanner.nextDouble();
                    scanner.nextLine(); 
                    if (banqueSystem.deposit(accountNumber, amount)) {
                        System.out.println("Deposit successful.");
                    }
                    break;
                case 2:
                    System.out.print("Enter account number: ");
                    accountNumber = scanner.nextLine();
                    System.out.print("Enter amount: ");
                    amount = scanner.nextDouble();
                    scanner.nextLine(); 
                    if (banqueSystem.withdraw(accountNumber, amount)) {
                        System.out.println("Withdrawal successful.");
                    }
                    break;
                case 3:
                    System.out.print("Enter source account number: ");
                    String fromAccount = scanner.nextLine();
                    System.out.print("Enter destination account number: ");
                    String toAccount = scanner.nextLine();
                    System.out.print("Enter amount: ");
                    amount = scanner.nextDouble();
                    scanner.nextLine(); 

                    if (banqueSystem.transfer(fromAccount, toAccount, amount, user.getId())) {
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

    private void showEditUserMenu() {
        System.out.println("=== Edit User Information ===");
        System.out.print("Enter user ID: ");
        int userId = scanner.nextInt();
        scanner.nextLine(); 

        User user = banqueSystem.getUserById(userId);
        if (user == null) {
            System.out.println("User not found.");
            return;
        }

        System.out.print("Enter new first name (current: " + user.getFirstName() + "): ");
        String firstName = scanner.nextLine();
        System.out.print("Enter new last name (current: " + user.getLastName() + "): ");
        String lastName = scanner.nextLine();
        System.out.print("Enter new email (current: " + user.getEmail() + "): ");
        String email = scanner.nextLine();
        System.out.print("Enter new password: ");
        String password = scanner.nextLine();
        System.out.print("Enter new role (ADMIN/CLIENT) (current: " + user.getRole() + "): ");
        String roleStr = scanner.nextLine();

        try {
            Role role = Role.valueOf(roleStr.toUpperCase());
            if (banqueSystem.updateUser(userId, firstName, lastName, email, password, role)) {
                System.out.println("User updated successfully.");
            } else {
                System.out.println("Failed to update user.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid role. Please enter ADMIN or CLIENT.");
        }
    }

    private void showDeleteUserMenu() {
        System.out.println("=== Delete User ===");
        System.out.print("Enter user ID: ");
        int userId = scanner.nextInt();
        scanner.nextLine(); 

        if (banqueSystem.deleteUser(userId)) {
            System.out.println("User deleted successfully.");
        } else {
            System.out.println("Failed to delete user.");
        }
    }

    private void showEditAccountMenu() {
        System.out.println("=== Edit Account Information ===");
        System.out.print("Enter account number: ");
        String accountNumber = scanner.nextLine();

        Account account = banqueSystem.getAccountByNumber(accountNumber);
        if (account == null) {
            System.out.println("Account not found.");
            return;
        }

        System.out.print("Enter new balance (current: " + account.getBalance() + "): ");
        double balance = scanner.nextDouble();
        scanner.nextLine(); 
        System.out.print("Enter new account type (current: " + account.getAccountType() + "): ");
        String accountType = scanner.nextLine();

        if (banqueSystem.updateAccount(accountNumber, balance, accountType)) {
            System.out.println("Account updated successfully.");
        } else {
            System.out.println("Failed to update account.");
        }
    }

    private void showChangePasswordMenu() {
        System.out.println("=== Change Password ===");
        System.out.print("Enter your current password: ");
        String oldPassword = scanner.nextLine();
        System.out.print("Enter your new password: ");
        String newPassword = scanner.nextLine();

        if (banqueSystem.changePassword(currentUser.getId(), oldPassword, newPassword)) {
            System.out.println("Password changed successfully.");
        } else {
            System.out.println("Failed to change password. Please check your current password.");
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
package presentation;

import services.BanqueSystem;
import models.User;
import models.Role;
import java.util.Scanner;

public class BankAppMenus {
    private Scanner scanner;
    private BanqueSystem banqueSystem;

    public BankAppMenus(Scanner scanner, BanqueSystem banqueSystem) {
        this.scanner = scanner;
        this.banqueSystem = banqueSystem;
    }

    public void showLoginMenu() {
        while (true) {
            System.out.println("=== Connexion ===");
            System.out.println("1. Se connecter");
            System.out.println("2. Quitter l'application");
            System.out.print("Choisissez une option : ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    System.out.print("Entrez votre e-mail : ");
                    String email = scanner.next();
                    System.out.print("Entrez votre mot de passe : ");
                    String password = scanner.next();

                    User user = banqueSystem.login(email, password);
                    if (user != null) {
                        if (user.getRole() == Role.ADMIN) {
                            showAdminMenu();
                        } else {
                            showClientMenu();
                        }
                    } else {
                        System.out.println("Échec de la connexion.");
                    }
                    break;

                case 2:
                    System.out.println("Merci d'avoir utilisé notre application. Au revoir !");
                    System.exit(0);

                default:
                    System.out.println("Option invalide. Veuillez réessayer.");
            }
        }
    }

    private void showAdminMenu() {
        while (true) {
            System.out.println("=== Menu Admin ===");
            System.out.println("1. Gérer les utilisateurs");
            System.out.println("2. Gérer les comptes");
            System.out.println("3. Gérer les transactions");
            System.out.println("4. Déconnexion");
            System.out.print("Choisissez une option : ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    showManageUsersMenu();
                    break;
                case 2:
                    showManageAccountsMenu();
                    break;
                case 3:
                    System.out.println("Gérer les transactions...");
                    break;
                case 4:
                    return; // Retour au menu de connexion
                default:
                    System.out.println("Option invalide. Veuillez réessayer.");
            }
        }
    }

    private void showClientMenu() {
        while (true) {
            System.out.println("=== Menu Client ===");
            System.out.println("1. Consulter mes comptes");
            System.out.println("2. Effectuer une opération");
            System.out.println("3. Consulter mes transactions");
            System.out.println("4. Modifier mes informations");
            System.out.println("5. Déconnexion");
            System.out.print("Choisissez une option : ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    System.out.println("Consulter mes comptes...");
                    break;
                case 2:
                    showPerformTransactionMenu();
                    break;
                case 3:
                    System.out.println("Consulter mes transactions...");
                    break;
                case 4:
                    System.out.println("Modifier mes informations...");
                    break;
                case 5:
                    return; // Retour au menu de connexion
                default:
                    System.out.println("Option invalide. Veuillez réessayer.");
            }
        }
    }

    private void showManageUsersMenu() {
        while (true) {
            System.out.println("=== Gérer les utilisateurs ===");
            System.out.println("1. Ajouter un utilisateur");
            System.out.println("2. Modifier un utilisateur");
            System.out.println("3. Supprimer un utilisateur");
            System.out.println("4. Afficher la liste des utilisateurs");
            System.out.println("5. Retour au menu principal");
            System.out.print("Choisissez une option : ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    System.out.println("Ajouter un utilisateur...");
                    break;
                case 2:
                    System.out.println("Modifier un utilisateur...");
                    break;
                case 3:
                    System.out.println("Supprimer un utilisateur...");
                    break;
                case 4:
                    System.out.println("Afficher la liste des utilisateurs...");
                    break;
                case 5:
                    return; // Retour au menu Admin
                default:
                    System.out.println("Option invalide. Veuillez réessayer.");
            }
        }
    }

    private void showManageAccountsMenu() {
        while (true) {
            System.out.println("=== Gérer les comptes ===");
            System.out.println("1. Créer un compte");
            System.out.println("2. Modifier un compte");
            System.out.println("3. Supprimer un compte");
            System.out.println("4. Afficher la liste des comptes");
            System.out.println("5. Retour au menu principal");
            System.out.print("Choisissez une option : ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    System.out.println("Créer un compte...");
                    break;
                case 2:
                    System.out.println("Modifier un compte...");
                    break;
                case 3:
                    System.out.println("Supprimer un compte...");
                    break;
                case 4:
                    System.out.println("Afficher la liste des comptes...");
                    break;
                case 5:
                    return; 
                default:
                    System.out.println("Option invalide. Veuillez réessayer.");
            }
        }
    }

    private void showPerformTransactionMenu() {
        while (true) {
            System.out.println("=== Effectuer une opération ===");
            System.out.println("1. Dépôt d'argent");
            System.out.println("2. Retrait d'argent");
            System.out.println("3. Transfert d'argent");
            System.out.println("4. Retour au menu principal");
            System.out.print("Choisissez une option : ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    System.out.println("Dépôt d'argent...");
                    break;
                case 2:
                    System.out.println("Retrait d'argent...");
                    break;
                case 3:
                    System.out.println("Transfert d'argent...");
                    break;
                case 4:
                    return; 
                default:
                    System.out.println("Option invalide. Veuillez réessayer.");
            }
        }
    }
}

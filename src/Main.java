import presentation.BankAppMenus;
import services.BanqueSystem;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        BanqueSystem banqueSystem = new BanqueSystem();

        BankAppMenus bankAppMenus = new BankAppMenus(scanner, banqueSystem);
        bankAppMenus.showLoginMenu();
    }
}
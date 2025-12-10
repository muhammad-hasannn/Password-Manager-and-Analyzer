package userinterface;

import java.util.Scanner;
import utils.InputUtils;

public class Menu {
    Scanner sc;
    InputUtils inputUtils;


    // Constructor
    public Menu(Scanner sc) {
        this.sc = sc;
        inputUtils = new InputUtils();
    }


    //---1. Main Menu---
    public int mainMenu() {
        System.out.println("\n╔═══════════════════════════════════╗");
        System.out.println("║  PASSWORD MANAGER & ANALYZER      ║");
        System.out.println("╚═══════════════════════════════════╝");
        System.out.println("1. Strength Analyzer");
        System.out.println("2. Password Manager");
        System.out.println("3. Close App");
        System.out.println();

        return inputUtils.getValidChoiceInt(sc, "Enter your choice: ", 1, 3);
    }

    //---2. Password Manager Main Menu---
    public int passwordManagerMain() {
        System.out.println("\n╔═══════════════════════════════════╗");
        System.out.println("║         PASSWORD MANAGER          ║");
        System.out.println("╚═══════════════════════════════════╝");
        System.out.println("1. Login");
        System.out.println("2. Signup");
        System.out.println("3. Back to Main Menu");
        System.out.println();

        return inputUtils.getValidChoiceInt(sc, "Enter your choice: ", 1, 3);
    }

    //---3. User Dashboard---
    public int userDashboard(String username) {
        System.out.println("\n╔═══════════════════════════════════╗");
        System.out.println("║  Welcome, " + username + "!");
        System.out.println("╚═══════════════════════════════════╝");
        System.out.println("1. Strength Analyzer");
        System.out.println("2. Password Manager");
        System.out.println("3. Sign Out");
        System.out.println();

        return inputUtils.getValidChoiceInt(sc, "Enter your choice: ", 1, 3);
    }

    //---4. Password Vault Menu---
    public int userVault() {
        System.out.println("\n╔═══════════════════════════════════╗");
        System.out.println("║        PASSWORD VAULT             ║");
        System.out.println("╚═══════════════════════════════════╝");
        System.out.println("1. Add New Password");
        System.out.println("2. View All Passwords");
        System.out.println("3. Delete Password");
        System.out.println("4. Update Password");
        System.out.println("5. Back to Main Menu");
        System.out.println();

        return inputUtils.getValidChoiceInt(sc, "Enter your choice: ", 1, 5);
    }
}

package utils;

import model.VaultData;
import security.CryptoHandler;

import java.util.ArrayList;
import java.util.Scanner;

public class DisplayUtils {
    Scanner sc;

    public DisplayUtils(Scanner sc){
        this.sc = sc;
    }

    //---Press Enter to Continue---
    public void pressEnterToContinue() {
        System.out.print("\nPress Enter to continue...");
        sc.nextLine();
    }

    //---Get Confirmation---
    public boolean getConfirmation(String message) {
        while (true) {
            System.out.print(message + " (y/n): ");
            String input = sc.nextLine().trim().toLowerCase();

            if (input.equals("y") || input.equals("yes")) {
                return true;
            } else if (input.equals("n") || input.equals("no")) {
                return false;
            }

            System.out.println("Please enter 'y' or 'n'");
        }
    }
    /**
     * <pre>
     * .trim()
     * Original string: "   Hello World   "
     * Trimmed string: "Hello World"
     * </pre>
     */

    //---Display: Application Title---
    public void showApplicationTitle() {
        System.out.println("\n");
        System.out.println("  ╔══════════════════════════════════════════════╗");
        System.out.println("  ║                                              ║");
        System.out.println("  ║      PASSWORD MANAGER & ANALYZER             ║");
        System.out.println("  ║                                              ║");
        System.out.println("  ║      Secure • Simple • Reliable              ║");
        System.out.println("  ║                                              ║");
        System.out.println("  ╚══════════════════════════════════════════════╝");
        System.out.println("\n");
    }

    //---Display: Goodbye Message---
    public void showGoodbyeMessage() {
        System.out.println("\n");
        System.out.println("  ╔══════════════════════════════════════════════╗");
        System.out.println("  ║                                              ║");
        System.out.println("  ║           Thank You For Using                ║");
        System.out.println("  ║      PASSWORD MANAGER & ANALYZER             ║");
        System.out.println("  ║                                              ║");
        System.out.println("  ║              Stay Secure!                    ║");
        System.out.println("  ║                                              ║");
        System.out.println("  ╚══════════════════════════════════════════════╝");
        System.out.println("\n");
    }

    //---Display: View all passwords
    public void viewAllPasswords(ArrayList<VaultData> data){

        // Check if empty
        if (data.isEmpty()) {
            System.out.println("\nNo passwords saved yet!");
            return;
        }

        System.out.println("\n╔════════════════════════════════════════════════╗");
        System.out.println("║            YOUR SAVED PASSWORDS                ║");
        System.out.println("╚════════════════════════════════════════════════╝");
        System.out.println("\nTotal passwords: " + data.size());
        System.out.println("─".repeat(50));

        int count = 1;
        for (VaultData v : data) {
            System.out.println("\nEntry #" + count);
            System.out.println("   Platform: " + v.getPlatform());
            System.out.println("   Username: " + v.getUsername());

            String plainPassword = CryptoHandler.decrypt(v.getPasswordEncrypted());
            System.out.println("   Password: " + plainPassword);
            System.out.println("─".repeat(50));
            count++;
        }
    }
}

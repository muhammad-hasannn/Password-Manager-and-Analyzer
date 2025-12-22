package utils;

import java.util.Scanner;

public class DisplayUtils {
    Scanner sc;

    public DisplayUtils(Scanner sc){
        this.sc = sc;
    }

    //---Utility: Press Enter to Continue---
    public void pressEnterToContinue() {
        System.out.print("\nPress Enter to continue...");
        sc.nextLine();
    }

    //---Utility: Get Confirmation---
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
    /*
        .trim()
        Original string: "   Hello World   "
        Trimmed string: "Hello World"
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
        System.out.println("  ║              Stay Secure! 🔐                 ║");
        System.out.println("  ║                                              ║");
        System.out.println("  ╚══════════════════════════════════════════════╝");
        System.out.println("\n");
    }
}

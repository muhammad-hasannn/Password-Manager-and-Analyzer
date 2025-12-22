package userinterface;

import model.User;
import model.VaultData;

import java.util.Scanner;

public class Form {
    Scanner sc;

    public Form(Scanner sc){
        this.sc = sc;
    }

    /*
     NOTE:
     all the validations:
         1. at the time of login
             - username validation (it must match)
             - password validation (that it must match)
               also before matching password will be hashed

         2.  at the time of signup
             - username uniqueness
             - password strength & hashing

         these all be done where ever they will be called

     at this moment both forms are doing same work, but in future we can add more attributes for signup,
     that's why diff methods
    */

    // 1. login form
    public User login(){
        System.out.println("\n╔═══════════════════════════════════╗");
        System.out.println("║              LOGIN                ║");
        System.out.println("╚═══════════════════════════════════╝");
        System.out.println();

        System.out.print("Enter your username: ");
        String username = sc.nextLine().trim();
        System.out.print("Enter your password: ");
        String password = sc.nextLine().trim();

        return new User(username, password);
    }

    // 2. Signup
    public User signup(){
        System.out.println("\n╔═══════════════════════════════════╗");
        System.out.println("║           CREATE ACCOUNT          ║");
        System.out.println("╚═══════════════════════════════════╝");
        System.out.println();

        System.out.print("Enter your username: ");
        String username = sc.nextLine().trim();
        System.out.print("Enter your password: ");
        String password = sc.nextLine().trim();

        return new User(username, password);

    }

    // 3. method to get vault data
    public VaultData takeVaultData(){
        System.out.println("\n╔═══════════════════════════════════╗");
        System.out.println("║           ADD VAULT DATA          ║");
        System.out.println("╚═══════════════════════════════════╝");
        System.out.println();

        System.out.print("Enter platform name: ");
        String platform = sc.nextLine().trim();
        System.out.print("Enter username: ");
        String username = sc.nextLine().trim();
        System.out.print("Enter password: ");
        String password = sc.nextLine().trim();

        return new VaultData(platform, username, password);
    }
}

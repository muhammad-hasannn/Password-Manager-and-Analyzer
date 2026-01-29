package userinterface;

import logindao.UserDAO;
import model.User;
import model.VaultData;
import security.CryptoHandler;
import services.StrengthAnalyzer;
import utils.DisplayUtils;

import java.util.List;
import java.util.Scanner;

public class Form {
    Scanner sc;

    public Form(Scanner sc){
        this.sc = sc;
    }

    /**
     * <pre>
     * NOTE:
     * the validations:
     *     1. at the time of login
     *         - username validation (it must match)
     *         - password validation (that it must match)
     *           also before matching password will be hashed
     *
     *     these all be done where ever they will be called
     * </pre>
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
        String plainPassword = sc.nextLine().trim();

        String hashedPassword = CryptoHandler.encrypt(plainPassword);

        return new User(username, hashedPassword);
    }

    // 2. Signup
    public User signup(){
        StrengthAnalyzer sa = new StrengthAnalyzer();
        UserDAO ud = new UserDAO();
        DisplayUtils du = new DisplayUtils(sc);

        System.out.println("\n╔═══════════════════════════════════╗");
        System.out.println("║           CREATE ACCOUNT          ║");
        System.out.println("╚═══════════════════════════════════╝");
        System.out.println();

        String username;
        while(true){
            System.out.print("Create username: ");
            username = sc.nextLine().trim();

            if(ud.searchUser(username)){
                System.out.println("\nUsername already exists!\n");

                // now asking if user want to exit
                if(du.getConfirmation("Do you want to exit?")){
                    return null;
                }
            }
            else{
                break;
            }
        }

        String password;
        while(true){
            System.out.print("Create password: ");
            password = sc.nextLine().trim();

            List<String> weakness = sa.analyzePassword(password);
            if(weakness.isEmpty()){
                System.out.println("\nYour password is strong.");
                break;
            }
            else{
                System.out.println("\nYour password has following weaknesses: \n");
                for(String s : weakness){
                    System.out.println("\t" + s);
                }
                System.out.println();
            }

        }

        String hashedPassword = CryptoHandler.encrypt(password);
        return new User(username, hashedPassword);
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

        String hashedPassword = CryptoHandler.encrypt(password);

        return new VaultData(platform, username, hashedPassword);
    }
}

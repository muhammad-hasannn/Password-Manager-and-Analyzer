package main;

import logindao.UserDAO;
import model.User;
import model.VaultData;
import security.CryptoHandler;
import services.StrengthAnalyzer;
import userinterface.Menu;
import userinterface.Form;
import utils.DisplayUtils;
import vaulthandler.FileOperations;
import vaulthandler.VaultManager;


import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {


    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Show welcome screen
        DisplayUtils displayUtils = new DisplayUtils(sc);

        Menu menu = new Menu(sc);
        Form form = new Form(sc);
        UserDAO userDAO = new UserDAO();
        FileOperations fileOps = new FileOperations();
        VaultManager vaultManager = null;
        String currentUser = null;
        StrengthAnalyzer sa = new StrengthAnalyzer();

        displayUtils.showApplicationTitle();

        // Start the app
        boolean running = true;
        while (running) {
            int choice = menu.mainMenu();

            // Strength Analyzer
            if (choice == 1) {

                System.out.print("Enter your password: ");
                String p = sc.nextLine();

                List<String> weakness = sa.analyzePassword(p);

                if(weakness == null){
                    System.out.println("Your password is secure.");
                }
                else{
                    System.out.println("Your password has the following weaknesses: ");
                    for(String w : weakness){
                        System.out.println(w);
                    }
                }
                displayUtils.pressEnterToContinue();
            }

            // Password Manager
            else if (choice == 2) {

                // Show login/signup menu
                int authChoice = menu.passwordManagerMain();

                // chooses to login
                if (authChoice == 1) {
                    User credentials = form.login();

                    // get user from database
                    User user = userDAO.getUser(credentials.getUsername());

                    // user have chooses to login, and have entered credentials
                    if (user != null) {
                        // encrypt entered password
                        String hashedPassword = CryptoHandler.encrypt(credentials.getPasswordHash());

                        // check if password matches
                        if (hashedPassword.equals(user.getPasswordHash())) {
                            currentUser = credentials.getUsername();
                            System.out.println("Login successful! Welcome, " + credentials.getUsername());
                            displayUtils.pressEnterToContinue();

                            // NOW user is logged in!
                            // We'll handle vault
                            if(currentUser != null){

                                // USER DASHBOARD
                                int dashChoice = menu.userDashboard(currentUser);

                                // Strength Analyzer
                                if (dashChoice == 1) {
                                    System.out.print("Enter your password: ");
                                    String p = sc.nextLine();

                                    List<String> weakness = sa.analyzePassword(p);

                                    if(weakness == null){
                                        System.out.println("Your password is secure.");
                                    }
                                    else{
                                        System.out.println("Your password has the following weaknesses: ");
                                        for(String w : weakness){
                                            System.out.println(w);
                                        }
                                    }
                                    displayUtils.pressEnterToContinue();

                                }

                                // My Vault
                                else if (dashChoice == 2) {
                                    handleVault(sc, menu, form, vaultManager, displayUtils);

                                }

                                else if (dashChoice == 3) {
                                    // Logout
                                    if(vaultManager.getAllPasswords() != null){
                                        if (displayUtils.getConfirmation("Save and logout?")) {
                                            // Save vault data
                                            fileOps.updateVault(currentUser, vaultManager.getAllPasswords());
                                        }
                                    }

                                    currentUser = null;
                                    vaultManager = null;
                                    System.out.println("Logged out!");
                                    displayUtils.pressEnterToContinue();
                                }

                            }

                        } else {
                            System.out.println("Wrong password!");
                            displayUtils.pressEnterToContinue();
                        }
                    } else {
                        System.out.println("User not found!");
                        displayUtils.pressEnterToContinue();
                    }

                } else if (authChoice == 2) {
                    // SIGNUP
                    User signupData = form.signup();
                    String username = signupData.getUsername();
                    String password = signupData.getPasswordHash();

                    // verifying the strength
                    List<String> weakness = sa.analyzePassword(password);
                    if(weakness != null){
                        System.out.println("Your password has the following weaknesses: ");
                        for(String w : weakness){
                            System.out.println(w);
                        }
                        continue;
                    }


                    // Check if username already exists
                    if (userDAO.searchUser(username)) {
                        System.out.println("Username already taken!");
                        displayUtils.pressEnterToContinue();
                    } else {
                        // Hash password
                        String hashedPassword = CryptoHandler.encrypt(password);

                        // Create user
                        User newUser = new User(username, hashedPassword);

                        // Add to database
                        if (userDAO.addUser(newUser)) {
                            System.out.println("Account created! You can now login.");
                            displayUtils.pressEnterToContinue();
                        } else {
                            System.out.println("Failed to create account!");
                            displayUtils.pressEnterToContinue();
                        }
                    }
                }
            }

            // Exit
            else if (choice == 3) {

                if (displayUtils.getConfirmation("Are you sure you want to exit?")) {
                    running = false;
                }
            }
        }
        displayUtils.showGoodbyeMessage();
        sc.close();
    }

    // 1. Helper method to handle vault operations
    private static void handleVault(Scanner scanner, Menu menu, Form form, VaultManager vault, DisplayUtils display) {
        boolean inVault = true;

        while (inVault) {
            int vaultChoice = menu.userVault();

            if (vaultChoice == 1) {
                // Add password
                VaultData newPassword = form.takeVaultData();

                if (vault.addNewPassword(newPassword)) {
                    System.out.println("Password added!");
                } else {
                    System.out.println("Password already exists!");
                }
                display.pressEnterToContinue();

            } else if (vaultChoice == 2) {
                // View all
                vault.viewAllPasswords();
                display.pressEnterToContinue();

            } else if (vaultChoice == 3) {
                // Delete password
                VaultData newPassword = form.takeVaultData();

                if (vault.deletePassword(newPassword)) {
                    System.out.println("Password deleted!");
                } else {
                    System.out.println("Password not found!");
                }
                display.pressEnterToContinue();

            } else if (vaultChoice == 4) {
                // Update password
                System.out.println("Enter old data: ");
                VaultData oldPassword = form.takeVaultData();

                System.out.println("Enter new data: ");
                VaultData newPassword = form.takeVaultData();

                if (vault.updatePassword(oldPassword, newPassword)) {
                    System.out.println("Password updated!");
                } else {
                    System.out.println("Password not found!");
                }

            } else if (vaultChoice == 5) {
                // Back
                inVault = false;
            }
        }
    }
}
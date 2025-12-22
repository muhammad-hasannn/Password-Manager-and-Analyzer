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

public class Main2 {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Show welcome screen
        DisplayUtils displayUtils = new DisplayUtils(sc);
        Menu menu = new Menu(sc);
        Form form = new Form(sc);
        UserDAO userDAO = new UserDAO();
        FileOperations fileOps = new FileOperations();
        StrengthAnalyzer sa = new StrengthAnalyzer();

        displayUtils.showApplicationTitle();

        // Start the app
        boolean running = true;
        while (running) {
            try {
                int choice = menu.mainMenu();

                // Strength Analyzer (Guest)
                if (choice == 1) {
                    handleStrengthAnalyzer(sc, sa, displayUtils);
                }

                // Password Manager
                else if (choice == 2) {
                    handlePasswordManager(sc, menu, form, userDAO, fileOps, sa, displayUtils);
                }

                // Exit
                else if (choice == 3) {
                    if (displayUtils.getConfirmation("Are you sure you want to exit?")) {
                        running = false;
                    }
                }

            } catch (Exception e) {
                System.out.println("\nAn error occurred: " + e.getMessage());
                displayUtils.pressEnterToContinue();
            }
        }

        displayUtils.showGoodbyeMessage();
        sc.close();
    }


    // Handle Strength Analyzer
    private static void handleStrengthAnalyzer(Scanner sc, StrengthAnalyzer sa, DisplayUtils displayUtils) {
        try {
            System.out.print("\nEnter your password: ");
            String p = sc.nextLine();

            if (p == null || p.trim().isEmpty()) {
                System.out.println("Password cannot be empty!");
                displayUtils.pressEnterToContinue();
                return;
            }

            List<String> weakness = sa.analyzePassword(p);

            if (weakness == null || weakness.isEmpty()) {
                System.out.println("Your password is secure.");
            } else {
                System.out.println("\nYour password has the following weaknesses:");
                for (String w : weakness) {
                    System.out.println("  • " + w);
                }
            }
            displayUtils.pressEnterToContinue();

        } catch (Exception e) {
            System.out.println("Error analyzing password: " + e.getMessage());
            displayUtils.pressEnterToContinue();
        }
    }


    // Handle Password Manager (Login/Signup)
    private static void handlePasswordManager(Scanner sc, Menu menu, Form form, UserDAO userDAO,
                                              FileOperations fileOps, StrengthAnalyzer sa,
                                              DisplayUtils displayUtils) {
        try {
            // Show login/signup menu
            int authChoice = menu.passwordManagerMain();

            // LOGIN
            if (authChoice == 1) {
                handleLogin(sc, menu, form, userDAO, fileOps, sa, displayUtils);
            }

            // SIGNUP
            else if (authChoice == 2) {
                handleSignup(form, userDAO, fileOps, sa, displayUtils);
            }

        } catch (Exception e) {
            System.out.println("Error in password manager: " + e.getMessage());
            displayUtils.pressEnterToContinue();
        }
    }


    // Handle Login
    private static void handleLogin(Scanner sc, Menu menu, Form form, UserDAO userDAO,
                                    FileOperations fileOps, StrengthAnalyzer sa,
                                    DisplayUtils displayUtils) {
        try {
            User credentials = form.login();

            if (credentials == null || credentials.getUsername() == null) {
                System.out.println("Invalid credentials!");
                displayUtils.pressEnterToContinue();
                return;
            }

            // Get user from database
            User user = userDAO.getUser(credentials.getUsername());

            if (user == null) {
                System.out.println("User not found!");
                displayUtils.pressEnterToContinue();
                return;
            }

            // Verify password
            String hashedPassword = CryptoHandler.encrypt(credentials.getPasswordHash());

            if (!hashedPassword.equals(user.getPasswordHash())) {
                System.out.println("Wrong password!");
                displayUtils.pressEnterToContinue();
                return;
            }

            // Login successful!
            String currentUser = credentials.getUsername();
            System.out.println("Login successful! Welcome, " + currentUser);
            displayUtils.pressEnterToContinue();

            //  LOAD VAULT DATA IMMEDIATELY
            ArrayList<VaultData> data = fileOps.getUserData(currentUser);
            VaultManager vaultManager = new VaultManager(data);

            // User dashboard loop
            boolean loggedIn = true;
            while (loggedIn) {
                try {
                    int dashChoice = menu.userDashboard(currentUser);

                    // Strength Analyzer
                    if (dashChoice == 1) {
                        handleStrengthAnalyzer(sc, sa, displayUtils);
                    }

                    // My Vault
                    else if (dashChoice == 2) {
                        handleVault(sc, menu, form, vaultManager, displayUtils);
                    }

                    // Logout
                    else if (dashChoice == 3) {
                        handleLogout(currentUser, vaultManager, fileOps, displayUtils);
                        loggedIn = false;
                    }

                } catch (Exception e) {
                    System.out.println("Error in dashboard: " + e.getMessage());
                    displayUtils.pressEnterToContinue();
                }
            }

        } catch (Exception e) {
            System.out.println("Login error: " + e.getMessage());
            displayUtils.pressEnterToContinue();
        }
    }


    // Handle Signup
    private static void handleSignup(Form form, UserDAO userDAO, FileOperations fileOps,
                                     StrengthAnalyzer sa, DisplayUtils displayUtils) {
        try {
            User signupData = form.signup();

            if (signupData == null || signupData.getUsername() == null || signupData.getPasswordHash() == null) {
                System.out.println("Invalid signup data!");
                displayUtils.pressEnterToContinue();
                return;
            }

            String username = signupData.getUsername();
            String password = signupData.getPasswordHash();

            // Verify password strength
            List<String> weakness = sa.analyzePassword(password);

            if (weakness != null && !weakness.isEmpty()) {
                System.out.println("\nYour password has the following weaknesses:");
                for (String w : weakness) {
                    System.out.println("  • " + w);
                }
                System.out.println("\nPlease choose a stronger password!");
                displayUtils.pressEnterToContinue();
                return;
            }

            // Check if username already exists
            if (userDAO.searchUser(username)) {
                System.out.println("Username already taken!");
                displayUtils.pressEnterToContinue();
                return;
            }

            // Hash password
            String hashedPassword = CryptoHandler.encrypt(password);

            // Create user
            User newUser = new User(username, hashedPassword);

            // Add to database
            if (userDAO.addUser(newUser)) {
                // Create vault file for new user
                fileOps.createNewUser(username);

                System.out.println("Account created! You can now login.");
                displayUtils.pressEnterToContinue();
            } else {
                System.out.println("Failed to create account!");
                displayUtils.pressEnterToContinue();
            }

        } catch (Exception e) {
            System.out.println("Signup error: " + e.getMessage());
            displayUtils.pressEnterToContinue();
        }
    }


    // Handle Logout
    private static void handleLogout(String currentUser, VaultManager vaultManager,
                                     FileOperations fileOps, DisplayUtils displayUtils) {
        try {
            if (vaultManager != null && displayUtils.getConfirmation("Save and logout?")) {
                // Save vault data
                ArrayList<VaultData> passwords = vaultManager.getAllPasswords();

                if (passwords != null) {
                    fileOps.updateVault(currentUser, passwords);
                    System.out.println("Data saved successfully!");
                }
            }

            System.out.println("Logged out!");
            displayUtils.pressEnterToContinue();

        } catch (Exception e) {
            System.out.println("Error during logout: " + e.getMessage());
            displayUtils.pressEnterToContinue();
        }
    }


    // Handle Vault Operations
    private static void handleVault(Scanner scanner, Menu menu, Form form,
                                    VaultManager vault, DisplayUtils display) {
        if (vault == null) {
            System.out.println("Vault not initialized!");
            display.pressEnterToContinue();
            return;
        }

        boolean inVault = true;

        while (inVault) {
            try {
                int vaultChoice = menu.userVault();

                // Add password
                if (vaultChoice == 1) {
                    VaultData newPassword = form.takeVaultData();

                    if (newPassword == null) {
                        System.out.println("Invalid password data!");
                        display.pressEnterToContinue();
                        continue;
                    }

                    if (vault.addNewPassword(newPassword)) {
                        System.out.println("Password added!");
                    } else {
                        System.out.println("Password already exists!");
                    }
                    display.pressEnterToContinue();
                }

                // View all
                else if (vaultChoice == 2) {
                    vault.viewAllPasswords();
                    display.pressEnterToContinue();
                }

                // Delete password
                else if (vaultChoice == 3) {
                    VaultData toDelete = form.takeVaultData();

                    if (toDelete == null) {
                        System.out.println("Invalid password data!");
                        display.pressEnterToContinue();
                        continue;
                    }

                    if (vault.deletePassword(toDelete)) {
                        System.out.println("Password deleted!");
                    } else {
                        System.out.println("Password not found!");
                    }
                    display.pressEnterToContinue();
                }

                // Update password
                else if (vaultChoice == 4) {
                    System.out.println("\nEnter old data:");
                    VaultData oldPassword = form.takeVaultData();

                    if (oldPassword == null) {
                        System.out.println("Invalid old password data!");
                        display.pressEnterToContinue();
                        continue;
                    }

                    System.out.println("\nEnter new data:");
                    VaultData newPassword = form.takeVaultData();

                    if (newPassword == null) {
                        System.out.println("Invalid new password data!");
                        display.pressEnterToContinue();
                        continue;
                    }

                    if (vault.updatePassword(oldPassword, newPassword)) {
                        System.out.println("Password updated!");
                    } else {
                        System.out.println("Password not found!");
                    }
                    display.pressEnterToContinue();
                }

                // Back
                else if (vaultChoice == 5) {
                    inVault = false;
                }

            } catch (Exception e) {
                System.out.println("Error in vault: " + e.getMessage());
                display.pressEnterToContinue();
            }
        }
    }
}
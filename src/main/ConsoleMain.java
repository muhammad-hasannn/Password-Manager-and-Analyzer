package main;

import logindao.DBConnection;
import model.User;
import model.VaultData;
import services.StrengthAnalyzer;
import userinterface.Menu;
import userinterface.Form;
import utils.DisplayUtils;
import logindao.UserDAO;
import vaulthandler.FileOperations;
import vaulthandler.VaultManager;

import java.util.ArrayList;
import java.util.Scanner;

public class ConsoleMain {

    /**
     * 4. Handling vault manager menu
     */
    public static void runVaultManager(Scanner sc, User user){

        Menu m = new Menu(sc);
        DisplayUtils du = new DisplayUtils(sc);
        FileOperations fo = new FileOperations();
        Form f = new Form(sc);

        // loading data in arraylist
        ArrayList<VaultData> data = fo.getUserData(user.getUsername());
        boolean isDataUpdated = false;

        VaultManager vm = new VaultManager(data);

        boolean isVaultRunning = true;
        while(isVaultRunning){
            int vaultManagerChoice = m.userVault();

            switch (vaultManagerChoice){

                // ADD NEW PASSWORD
                case 1:
                    isDataUpdated = true;
                    VaultData newEntry = f.takeVaultData();

                    if(vm.addNewPassword(newEntry))
                        System.out.println("\nVault updated!\n");
                    else
                        System.out.println("\nPassword already exists!\n");
                break;

                // VIEW ALL PASSWORDS
                case 2:
                    du.viewAllPasswords(data);
                break;

                // DELETE PASSWORD
                case 3:
                    isDataUpdated = true;

                    System.out.println("\nEnter credentials of password to delete: \n");
                    VaultData dataToDelete = f.takeVaultData();

                    if(vm.deletePassword(dataToDelete))
                        System.out.println("Data deleted!");
                    else
                        System.out.println("Data not exists!");
                break;

                // UPDATE PASSWORD
                case 4:
                    isDataUpdated = true;

                    System.out.println("Enter credentials of old data: \n");
                    VaultData oldData = f.takeVaultData();

                    System.out.println("Enter credentials for new data: \n");
                    VaultData newData = f.takeVaultData();

                    if(vm.updatePassword(oldData, newData))
                        System.out.println("Updated Successfully!");
                    else
                        System.out.println("Data does not exists!");
                break;

                // BACK TO MAIN MENU
                case 5:
                    if(du.getConfirmation("Do you want to go to dashboard? ")){
                        isVaultRunning = false;
                    }
                break;
            }
        }

        // sending the updated data
        if(isDataUpdated) fo.updateVault(user.getUsername(), data);
        else System.out.println("Unable to save user vault data.");
                
    }

    /**
     * 3. Handling user dashboard menu
     */
    public static void runUserDashboard(Scanner sc, User user){

        DisplayUtils du = new DisplayUtils(sc);
        Menu m = new Menu(sc);
        StrengthAnalyzer sa = new StrengthAnalyzer();

        boolean isUserLogin = true;
        while(isUserLogin){
            int userDashboardChoice = m.userDashboard(user.getUsername());

            switch (userDashboardChoice){

                // STRENGTH ANALYZER
                case 1:
                    sa.useStrengthAnalyzer(sc);
                break;

                // MANAGE VAULT
                case 2:
                    runVaultManager(sc, user);
                break;

                // SIGN OUT
                case 3:
                    if(du.getConfirmation("Do you want to sign out? ")){
                        isUserLogin = false;
                    }
                break;
            }
        }
    }


    /**
     * 2. Handling Password Manager Main Menu
     */
    public static void runPasswordManagerMain(Scanner sc){

        DisplayUtils du = new DisplayUtils(sc);
        Menu m = new Menu(sc);
        Form f = new Form(sc);
        UserDAO ud = new UserDAO();
        FileOperations fo = new FileOperations();

        boolean isPasswordManagerRunning = true;

        while(isPasswordManagerRunning){
            int passwordManagerMainChoice = m.passwordManagerMain();

            switch (passwordManagerMainChoice){

                // LOGIN
                case 1:
                    User user = f.login();
                    User dbUser = ud.getUser(user.getUsername());

                    if(dbUser == null){
                        System.out.println("Invalid credentials!");
                    }
                    else{
                        // login successfully
                        if(user.equals(dbUser)){
                            runUserDashboard(sc, user);
                        }
                        else{
                            System.out.println("Invalid credentials!");
                        }
                    }
                break;

                // SIGN UP
                case 2:
                    User newUser = f.signup();

                    if(newUser != null){
                        if(ud.addUser(newUser)){
                            System.out.println("\nAccount created!\n");
                            // also creating user header in file (vault)
                            fo.createNewUser(newUser.getUsername());
                        }
                        else System.out.println("Error in creating new user.");


                    }
                break;

                // BACK TO MAIN MENU
                case 3:
                    if(du.getConfirmation("Do you to go to main menu? ")){
                        isPasswordManagerRunning = false;
                    }
                break;
            }
        }
    }

    /**
     * 1. Handling Main Menu
     */
    public static void main(String[] args) {

        // first of all checking connection
        if(!DBConnection.testConnection()) return;

        Scanner sc = new Scanner(System.in);
        DisplayUtils du = new DisplayUtils(sc);
        Menu m = new Menu(sc);
        StrengthAnalyzer sa = new StrengthAnalyzer();


        du.showApplicationTitle();
        boolean isProgramRunning = true;

        while(isProgramRunning){

            int mainMenuChoice = m.mainMenu();

            switch (mainMenuChoice){

                // STRENGTH ANALYZER
                case 1:
                    sa.useStrengthAnalyzer(sc);
                break;

                // PASSWORD MANAGER
                case 2:
                    runPasswordManagerMain(sc);
                break;

                // CLOSE PROGRAM
                case 3:
                    if(du.getConfirmation("Do you want to exit? ")){
                        du.showGoodbyeMessage();
                        isProgramRunning = false;
                    }
                break;
            }
        }

    }
}

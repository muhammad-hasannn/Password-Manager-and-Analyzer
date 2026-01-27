package main;

import model.User;
import services.StrengthAnalyzer;
import userinterface.Menu;
import userinterface.Form;
import utils.DisplayUtils;
import logindao.UserDAO;

import java.util.Scanner;

public class ConsoleMain {

    /**
     * 4. Handling vault manager menu
     */
    public static void runVaultManager(Scanner sc, User user){
        System.out.println("Soon...");
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
                        ud.addUser(newUser);
                        System.out.println("\nAccount created!\n");
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

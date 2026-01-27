package utils;

import java.util.InputMismatchException;
import java.util.Scanner;


public class InputUtils {

    // 1. method to take integer input
    public int getIntInput(Scanner sc, String message){
        int var;

        while(true){
            try{
                System.out.print(message);
                var = sc.nextInt();
                sc.nextLine();

                return var;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input! Please enter a number.");
                sc.nextLine();
            }
        }
    }

    // 2. method to take valid integer + valid choice
    public int getValidChoiceInt(Scanner sc, String message, int min, int max){
        int var;

        // this loop will keep running until the valid choice is taken from user.
        // as soon user enters the valid choice, it will return it at the moment
        while(true){
            try{
                var = getIntInput(sc, message);
                if(var < min || var > max)
                    throw new InvalidChoiceException("Please enter between " + min + " and " + max);

                return var;
            } catch (InvalidChoiceException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    // 3. Method to take Strength analyzer input
    public String getStrengthAnalyzerInput(Scanner sc){
        System.out.print("\nEnter your password: ");

        return sc.nextLine();
    }

}

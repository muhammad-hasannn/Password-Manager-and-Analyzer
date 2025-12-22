package utils;

import java.util.InputMismatchException;
import java.util.Scanner;


public class InputUtils {

    // 1. method to take integer input
    public int getIntInput(Scanner sc, String message){
        int var = 0;

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
        int var = 0;

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
}

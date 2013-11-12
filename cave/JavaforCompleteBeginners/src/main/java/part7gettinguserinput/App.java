/*
 * Here comes the text of your license
 * Each line should be prefixed with  * 
 */
package part7gettinguserinput;

/**
 * How to get console user input in your Java programs using the JDK Scanner
 * class.
 */
import java.util.Scanner;

public class App {

    public static void main(String[] args) {

        // Create scanner object
        Scanner input = new Scanner(System.in);

        // Output the prompt
        System.out.println("Enter a floating point value: ");

        // Wait for the user to enter something.
        double value = input.nextDouble();

        // Tell them what they entered.
        System.out.println("You entered: " + value);
    }
}

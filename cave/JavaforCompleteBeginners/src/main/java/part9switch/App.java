package part9switch;

/**
 * How to use the switch statement in Java; a construct that many programmers
 * neglect but that invariably appears early on in tests and courses!
 */
import java.util.Scanner;

public class App {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.println("Please enter a command: ");
        String text = input.nextLine();
        switch (text) {
            case "start":
                System.out.println("Machine started!");
                break;
            case "stop":
                System.out.println("Machine stopped.");
                break;
            default:
                System.out.println("Command not recognized");
        }
    }

}

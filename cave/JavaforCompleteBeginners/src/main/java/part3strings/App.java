package part3strings;

/**
 * we look at working with text using the String class. We also get our first
 * real peek at using classes and objects.
 */
public class App {

    public static void main(String[] args) {
        int myInt = 7;
        String text = "Hello";
        String blank = " ";
        String name = "Bob";
        String greeting = text + blank + name;

        System.out.println(greeting);
        System.out.println("Hello" + " " + "Bob");
        System.out.println("My integer is: " + myInt);

        double myDouble = 7.8;
        System.out.println("My number is: " + myDouble + ".");
    }
}

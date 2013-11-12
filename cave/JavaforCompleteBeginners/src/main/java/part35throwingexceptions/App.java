package part35throwingexceptions;

import java.io.IOException;

/**
In this tutorial we’ll take a look at how to throw your own exceptions in Java. 
* Throwing an exception is a useful thing to do when your method encounters an 
* unexpected error; the exception will immediately return from your method, 
* enabling you to handle the problem further up the call stack.
 */
public class App {
    public static void main(String[] args) {
        Test test = new Test();
        try {
            test.run();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}

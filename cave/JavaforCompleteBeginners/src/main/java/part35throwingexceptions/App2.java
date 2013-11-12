package part35throwingexceptions;
/**
In this tutorial we’ll take a look at how to throw your own exceptions in Java. 
* Throwing an exception is a useful thing to do when your method encounters an 
* unexpected error; the exception will immediately return from your method, 
* enabling you to handle the problem further up the call stack.
 */
public class App2 {
    public static void main(String[] args) {
        Test2 test2 = new Test2();
        try {
            test2.run();
        } catch (ServerException ex) {
            System.out.println(ex.getMessage());
        }
    }
}

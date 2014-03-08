package inheritance;

public class App {

    public static void someMethod(Number n) { /* ... */ }

    public static void main(String[] args) {
        Object someObject = new Object();
        Integer someInteger = new Integer(10);
        someObject = someInteger; // OK

        someMethod(new Integer(10));   // OK
        someMethod(new Double(10.1));   // OK

    }
}

package part37runtimeexceptions;

/**
 * Runtime exceptions are a kind of exception that you’re not forced to handle.
 * They usually point to serious problems with your code, indicating that you
 * need to revise it. The infamous null pointer exception and array
 * out-of-bounds exception are good examples. Examiners and interviewers love to
 * ask about the two main exception types in Java, so be prepared!
 */
public class App {

    public static void main(String[] args) {

        // Null pointer exception ....
        String text = null;

        //System.out.println(text.length());

        // Arithmetic exception ... (divide by zero)
        //int value = 7 / 0;

        // You can actually handle RuntimeExceptions if you want to;
        // for example, here we handle an ArrayIndexOutOfBoundsException
        String[] texts = {"one", "two", "three"};

        try {
            System.out.println(texts[3]);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println(e.toString());
        }
    }
}

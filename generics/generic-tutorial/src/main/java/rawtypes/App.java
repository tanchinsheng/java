package rawtypes;

/**
 * Hello world!
 *
 */
public class App {

    public static void main(String[] args) {

        // To create a parameterized type of Box<T>,
        // you supply an actual type argument for the formal type parameter T:
        Box<Integer> intBox = new Box<>();

        // If the actual type argument is omitted,
        // you create a raw type of Box<T>:
        Box rawBox = new Box();

        Box<String> stringBox = new Box<>();
        Box myRawBox = stringBox;               // OK
        myRawBox.set(8);  // warning: unchecked invocation to set(T)

    }
}

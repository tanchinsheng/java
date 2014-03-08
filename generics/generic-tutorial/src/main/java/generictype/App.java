package generictype;

/**
 * Hello world!
 *
 */
public class App {

    public static void main(String[] args) {
        Box<Integer> integerBox = new Box<>();

        Pair<String, Integer> p1 = new OrderedPair<>("Even", 8);
        Pair<String, String> p2 = new OrderedPair<>("hello", "world");

        OrderedPair<String, Box<Integer>> p = new OrderedPair<>("primes", new Box<Integer>());
    }
}

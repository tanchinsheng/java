package genricmethods;

/**
 * Hello world!
 *
 */
public class App {

    public static void main(String[] args) {
        Pair<Integer, String> p1 = new Pair<>(1, "apple");
        Pair<Integer, String> p2 = new Pair<>(2, "pear");
        boolean same = Util.<Integer, String>compare(p1, p2);
        System.out.println(same);

        Pair<Integer, String> p3 = new Pair<>(1, "apple");
        Pair<Integer, String> p4 = new Pair<>(2, "pear");
        boolean same2 = Util.compare(p3, p4); // type inference
        System.out.println(same2);
    }
}

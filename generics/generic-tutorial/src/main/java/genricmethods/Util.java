package genricmethods;

public class Util {

    // Generic static method
    // The syntax for a generic method includes a type parameter,
    // inside angle brackets, and appears before the method's return type.
    public static <K, V> boolean compare(Pair<K, V> p1, Pair<K, V> p2) {
        return p1.getKey().equals(p2.getKey())
                && p1.getValue().equals(p2.getValue());
    }
}

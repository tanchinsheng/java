package q084.javadatatypes;

public class app {

    public static void main(String[] args) {
        Integer i = new Integer(42);
        Long ln = new Long(42);
        Double d = new Double(42.0);

        if (i == ln) {
        };
        if (ln == d) {
        };
        if (i.equals(d)) {
        };
        if (d.equals(ln)) {
        };
        //Due to auto-boxing int 42 is converted into an Integer object containing 42.
        // So this is valid. It will return false though because ln is a Long and 42 is boxed into an Integer.
        if (ln.equals(43)) {
        };

    }

}

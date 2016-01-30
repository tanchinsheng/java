package q106.javadatatypes;

public class app {

    public static void main(String[] args) {
        Integer a = new Integer(1);
        Integer b = new Integer(2);
        Integer c = new Integer(3);
        Float d = new Float(3.0f);

        if (a.equals(a)) {
            System.out.println("true1");
        }
        if (b.equals(c)) {
            System.out.println("true2");
        }
        // The wrapper classes's equals() method overrides Object's equals() method
        // to compare the actual value instead of the reference.
        if (a.equals(b)) {
            System.out.println("true3");
        }
        if (c.equals(d)) {
            System.out.println("true4");
        }
    }

}

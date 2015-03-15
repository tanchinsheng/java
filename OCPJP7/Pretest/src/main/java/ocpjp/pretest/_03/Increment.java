package ocpjp.pretest._03;

/**
 * Hello world!
 *
 */
public class Increment {

    public static void main(String[] args) {
        // Integer are immutable objects. If there is an Integer objects for a value
        // that already exisits, then it does not create a new object again.
        // In other words, Java uses sharing of immutable objects, so two
        // Integer objects are equals if their values (no matter if you use
        // == operators to compare the references or use equals() method to
        // compare  the contents).

        Integer i = 10;
        Integer j = 11;
        Integer k = ++i; // INCR
        System.out.println("k==j is " + (k == j));
        //true
        System.out.println("k.equals(j) is " + (k.equals(j)));
        //true

    }
}

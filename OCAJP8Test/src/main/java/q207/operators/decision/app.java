package q207.operators.decision;

/**
 *
 * What is the result of executing the following fragment of code:
 */
public class app {

    /**
     * All an if() needs is a boolean. Now, b1 != b2 returns false which is a
     * boolean and so the expression becomes b2 = false.  It returns false which
     * is again a boolean. So there is no error and it prints false. Remember
     * that every expression has a return value (which is actually the Left Hand
     * Side of the expression). For example, The value of the expressing i = 10
     * , is 10 (an int).
     */
    public static void main(String[] args) {
        boolean b1 = false;
        boolean b2 = false;
        if (b2 = b1 != b2) {
            System.out.println("true");
        } else {
            System.out.println("false");
        }
    }

}

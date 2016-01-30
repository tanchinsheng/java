package q183.operators.decision;

public class app {

    public static void main(String[] args) {
        Integer i1 = 1;
        Integer i2 = new Integer(1);
        int i3 = 1;
        Byte b1 = 1;
        Long g1 = 1L;
        if (i1 == i2) { // This will return false because both are pointing to different objects.
            System.out.println("true1");
        }
        if (i1 == i3) { //  This will return true because one operand is a primitive int,
            // so the other will be unboxed and then the value will be compared.
            System.out.println("true2");
        }

        if (i1.equals(i2)) { // This will return true because both are Integer objects and both have the value 1.
            System.out.println("true4");
        }
        if (i1.equals(g1)) {
            System.out.println("true5");
        } // This will return false because they are pointing to objects of different types.
        // Signature of equals method is : boolean equals(Object o);
        // Thus, it can take any object as a parameter and so there will be no compilation error.
        // Further, The equals method of all wrapper classes first checks if the two object
        // are of same class or not. If not, they immediately return false.
        if (i1.equals(b1)) {
            System.out.println("true6");
        } //This will return false because they are pointing to objects of different types.

        if (i1 == b1) {
            System.out.println("true3");
        }
        // This will not compile because type of i1 and b1 references are classes that are
        // not in the same class hierarchy. So the compiler figures out at compile time itself
        // these two references cannot ever point to the same object.

    }

}

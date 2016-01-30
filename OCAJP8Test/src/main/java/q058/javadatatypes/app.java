package q058.javadatatypes;

public class app {

    public static void main(String[] args) {
        int i1 = 1, i2 = 2, i3 = 3;
        int i4 = i1 + (i2 = i3);
        System.out.println(i4); //4
        // First the value of i1 is evaluated (i.e. 1).
        // Now, i2 is assigned the value of i3 i.e. i2 becomes 3.
        // Finally i4 gets 1 +3 i.e. 4.

    }

}

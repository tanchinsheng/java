package q219.arrays;

public class Test {

    //https://docs.oracle.com/javase/specs/jls/se8/html/jls-15.html#jls-15.10.1
    public static int[] getArray() {
        // int i = 1 / 0; // Line 1
        return null;
    }

    public static void main(String[] args) {

        int[] a = {11, 12, 13, 14};
        int[] b = {0, 1, 2, 3};
        // System.out.println((a = b)[3]);
        System.out.println(a[(a = b)[3]]);

        int index = 1;
        try {
            getArray()[index = 2]++;
// if Line 1 is there, things in bracket would not be executed.
        } catch (Exception e) {
        }//empty catch
        System.out.println("index = " + index);
    }
}

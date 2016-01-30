package q060.javadatatypes;

public class TestClass {

    static boolean b;
    static int[] ia = new int[1];
    static char ch;
    static boolean[] ba = new boolean[1];

    public static void main(String[] args) {
        boolean x = false;
        if (b) {
            x = (ch == ia[ch]);
        } else {
            x = (ba[ch] = b);
        }
        System.out.println(x + " " + ba[ch]);
    }
}

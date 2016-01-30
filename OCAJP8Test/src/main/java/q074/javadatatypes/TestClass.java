package q074.javadatatypes;

public class TestClass {

    public static void main(String[] args) throws Exception {
        int a = Integer.MIN_VALUE;
        System.out.println(Integer.toBinaryString(Integer.MIN_VALUE) + " " + Integer.MIN_VALUE);
        System.out.println(Integer.toBinaryString(Integer.MAX_VALUE) + " " + Integer.MAX_VALUE);

        int b = -a;
        System.out.println(a + "   " + b);
    }
}

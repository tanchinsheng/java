package q102.javadatatypes;

public class TestClass {

    public static void main(String[] args) {
        Object a, b, c;
        a = new String("A");
        b = new String("B");
        c = a;
        a = b;
        System.out.println("" + c);
    }

}

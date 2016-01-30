package q327.methods;

public class InitTest {

    static String s1 = sM1("a");

    {
        s1 = sM1("b");
    }

    static {
        s1 = sM1("c");
    }

    private static String sM1(String s) {
        System.out.println(s);
        return s;
    }

    public static void main(String[] args) {
        InitTest it = new InitTest();
    }

}

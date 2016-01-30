package q125.operators.decision;

public class app {

    public static void main(String[] args) {
        short s1 = Short.MAX_VALUE;
        char c = s1;
        System.out.println(c == Short.MAX_VALUE);

        short s2 = 1;
        byte b1 = s2;

        final short s3 = 1;
        byte b2 = s3;

        final short s4 = 200;
        byte b3 = s4;
    }
}

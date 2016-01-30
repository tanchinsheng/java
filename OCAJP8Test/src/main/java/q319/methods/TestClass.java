package q319.methods;

public class TestClass {

    char c; // c is an instance variable of numeric type so it will be given a default value of 0, which prints as empty space.

    public void m1() {
        char[] cA = {'a', 'b'};
        m2(c, cA);
        System.out.println(((int) c) + "," + cA[1]);
        // 0,m : Because of the explicit cast to int in the println() call, c will be printed as 0.
    }

    public void m2(char c, char[] cA) {
        c = 'b';
        cA[1] = cA[0] = 'm';
    }

    public static void main(String[] args) {
        new TestClass().m1();
    }

}

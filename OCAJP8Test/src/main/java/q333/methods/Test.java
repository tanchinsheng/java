package q333.methods;

public class Test {

    static int a = 0;
    int b = 5;

    public void foo() {
        while (b > 0) {
            b--;
            a++;
        }
    }

    public static void main(String[] args) {
        Test p1 = new Test();
        p1.foo();
        Test p2 = new Test();
        p2.foo();
        System.out.println(p1.a + " " + p2.a);
    }

}

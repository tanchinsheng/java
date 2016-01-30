package q281.loop;

public class TestClass {

    int x = 5;

    int getX() {
        return x;
    }

    public void looper() {
        int x = 0;
        while ((x = getX()) != 0) {
            for (int m = 10; m >= 0; m--) {
                x = m;
                System.out.print(x + " ");
            }
            System.out.println();
        }
    }

    public static void main(String args[]) throws Exception {
        TestClass tc = new TestClass();
        tc.looper();
        System.out.println(tc.x);
    }

}

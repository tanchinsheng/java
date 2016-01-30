package q336.methods;

public class StaticTest {

    void m1() {
        StaticTest.m2();  // 1
        m4();             // 2
        StaticTest.m3();  // 3
    }

    static void m2() {
    }  // 4

    void m3() {
        m1();            // 5
        m2();            // 6
        StaticTest.m1(); // 7
    }

    static void m4() {
    }

    public static void main(String[] args) {
        // TODO code application logic here
    }

}

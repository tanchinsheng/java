package q378.inheritance;

interface T1 {

    int VALUE = 1;

    void m1();
}

interface T2 {

    int VALUE = 2;

    void m1();
}

class TestClass implements T1, T2 {

    public void m1() {
    }

    public static void main(String[] args) {
        TestClass tc = new TestClass();
        System.out.println(((T1) tc).VALUE);
        ((T2) tc).m1();
        tc.m1();
        System.out.println(tc.VALUE);
    }

}

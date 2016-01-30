package q412.inheritance;

class TestA {

    int i = 10;

    public static void m1() {
        System.out.println("A's m1");
    }

    public void m2() {
        System.out.println("A's m2");
    }
}

public class TestB extends TestA {

    int i = 20;

    public static void m1() {
        System.out.println("B's m1");
    }

    public void m2() {
        System.out.println("B's m2");
    }

    public static void main(String[] args) {
        TestA a = new TestB();
        System.out.println(a.i); //will print 10 instead of 20

        a.m1();  //will call A's m1
        a.m2();  //will call B's m2 as m2() is not static and so overrides A's m2()
    }

}

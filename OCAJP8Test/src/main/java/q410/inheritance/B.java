package q410.inheritance;

abstract class A {

    protected int m1() {
        return 0;
    }
}

public class B extends A {

    int m1() { // The overriding method cannot decrease the accessibility.
        return 1;
    }

    public static void main(String[] args) {
        // TODO code application logic here
    }

}

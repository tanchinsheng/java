package q407.inheritance;

class A {

    A() {
        this("hello", " world");
    }

    A(String s) {
        System.out.println(s);
    }

    A(String s1, String s2) {
        this(s1 + s2);
    }
}

class B extends A {

    B() {
        super("good bye");
    }

    ;
    B(String s) {
        super(s, " world");
    }

    B(String s1, String s2) {
        this(s1 + s2 + " ! ");
    }
}

public class TestClass {

    public static void main(String args[]) {
        A b = new B("good bye");
    }
}

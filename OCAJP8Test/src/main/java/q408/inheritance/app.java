package q408.inheritance;

class A {

    public void mA() {
    }
;

}

class B extends A {

    public void mA() {
    }

    public void mB() {
    }
}

class C extends B {

    public void mC() {
    }
}

public class app {

    public static void main(String[] args) {
        A x = new B();
        x.mA();
        x.mB();

        B y = new B();
        y.mA();

        B z = new C();
        z.mC();
        z.mB();
    }

}

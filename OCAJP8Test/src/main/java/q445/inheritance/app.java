package q445.inheritance;

interface I {
}

class A implements I {
}

class B extends A {
}

class C extends B {
}

public class app {

    public static void main(String[] args) {
        A a = new A();
        B b = new B();

        //     a = (B) (I) b; //ok: class B does implement I because it extends A, which implements I.
        // A reference of type I can be cast to any class at compile time. Since B is-a A, it can be assigned to a
        //      b = (B) (I) a; // This will fail at run time because a does not point to an object of class B.
        //  I i = (C) a; // It will compile because a C is-a A, which is-a I, and a reference of class A
        // can point to an object of class C. But it will fail at runtime because a does not point to an object of class C.
        a = (I) b; //An I is not an A. Therefore, it will not compile.
    }

}

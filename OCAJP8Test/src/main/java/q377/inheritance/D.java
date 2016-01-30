package q377.inheritance;

interface X1 { // C is-a X1: Because C is-a B and B is-a X1.
}

interface X2 { // C is-a X2: Because C implements X2
}

class A {
}

class B extends A implements X1 {
}

class C extends B implements X2 { // C is-a A: Because C 'is-a' B and B 'is-a' A.

    D d = new D();
} // C has-a D. Not B has a D.

class D {

    public static void main(String[] args) {
        // TODO code application logic here
    }

}

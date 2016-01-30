package q389.inheritance;

class A {
}

class B extends A {
}

class C extends B {
}

class X {

    B getB() {
        return new B();
    }
}

class Y extends X {

    //method declaration here
    public static void main(String[] args) {
        // TODO code application logic here
    }

}

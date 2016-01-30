package q409.inheritance;

class A {

    A() {
        print();
    }

    //private void print()
    void print() {
        System.out.println("A");
    }
}

class B extends A {

    int i = 4;

    void print() {
        System.out.println(i);
    }

    public static void main(String[] args) {

        A a = new B();
        a.print();

        //0,4
    }

}

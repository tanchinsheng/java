package q413.inheritance;

class A {

    public void m1() {
    }
}

class B extends A {

    public void m1() {
    }
}

class C extends B {

    public void m1() {
        /*  //1
         ... lot of code.
         */
    }

    public static void main(String[] args) {
        ((A) this).m1();
    }

}

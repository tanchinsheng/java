package q373.inheritance;

interface I {

    void m1();
    // same as public abstract void m1();
}

abstract class Klass {

    void m1() {
    }
;

}

class SubClass extends Klass implements I {

    public void m1() {
    }

    public static void main(String[] args) {
        // TODO code application logic here
    }

}

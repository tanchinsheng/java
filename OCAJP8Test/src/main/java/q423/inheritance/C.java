package q423.inheritance;

class A {

    public void perform_work() {
        System.out.println("A");
    }
}

class B extends A {

    public void perform_work() {
        System.out.println("B");
    }
}

public class C extends B {

    public void perform_work() {
        System.out.println("C");
    }

    public static void main(String[] args) {
        C c = new C();
    }

}

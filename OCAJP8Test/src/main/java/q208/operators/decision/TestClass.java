package q208.operators.decision;

public class TestClass {

    //define tester method here
    public boolean tester1() {
        return false;
    }

    public Boolean tester2() {
        return false;
    }

    public static void main(String[] args) {
        TestClass tc = new TestClass();
        while (tc.tester1()) {
            System.out.println("running...");
        }
        while (tc.tester2()) {
            System.out.println("running...");
        }
    }

}

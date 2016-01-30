package q441.inheritance;

public class TestClass {

    static int si = 10;

    public TestClass() {
        System.out.println(this);
    }

    public String toString() {
        return "TestClass.si = " + this.si;
    }

    public static void main(String[] args) {
        new TestClass();
    }

}

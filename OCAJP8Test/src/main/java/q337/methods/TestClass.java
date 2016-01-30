package q337.methods;

class A {

    String value = "test";

    A(String val) {
        this.value = val;
    }
}

public class TestClass {

    public static void main(String[] args) {
        new A("new test").print();
    }

}

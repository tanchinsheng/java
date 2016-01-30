package q320.methods;

public class TestClass {

    static TestClass ref;
    String[] arguments;

    public void func(String[] args) {
        ref.arguments = args;
    }

    public static void main(String args[]) {
        ref = new TestClass();
        ref.func(args);
    }

}

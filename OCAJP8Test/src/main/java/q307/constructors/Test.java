package q307.constructors;

public class Test {

    Test(Test b) { //The constructor can take the same type as a parameter.
    }

    Test Test() { // A constructor cannot return anything.
    }

    private final Test() { //  A constructor cannot be final, static or abstract.
    }

    void Test() { // A constructor cannot return anything. Not even void.
    }

    public static void Test(String args[]) { //A constructor cannot be final, static or abstract.
    }

    public static void main(String[] args) {
        // TODO code application logic here
    }

}

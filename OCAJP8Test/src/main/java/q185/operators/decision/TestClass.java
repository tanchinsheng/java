package q185.operators.decision;

public class TestClass {

    public static void main(String[] args) throws Exception {
        boolean flag = true;
        switch (flag) {  // A boolean cannot be used for a switch statement. It needs an integral type, an enum, or a String.
            case true:
                System.out.println("true");
            default:
                System.out.println("false");
        }
    }

}

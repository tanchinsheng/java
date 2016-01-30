package q127.operators.decision;

public class TestClass {

    public static void main(String args[]) {
        int x = Integer.parseInt(args[0]);

        switch (x) {
            case x < 5:
                System.out.println("BIG");
                break;
            case x > 5:
                System.out.println("SMALL");
            default:
                System.out.println("CORRECT");
                break;
        }
    }
}

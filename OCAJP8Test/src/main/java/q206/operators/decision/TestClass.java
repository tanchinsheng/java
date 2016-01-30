package q206.operators.decision;

public class TestClass {

    public void switchString(String input) {
        switch (input) {
            case "a":
                System.out.println("apple");
            case "b":
                System.out.println("bat");
                break;
            case "c":
                System.out.println("cat");
            default:
                System.out.println("none");
        }
    }

    public static void main(String[] args) {
        TestClass tc = new TestClass();
        tc.switchString("c");
    }

}

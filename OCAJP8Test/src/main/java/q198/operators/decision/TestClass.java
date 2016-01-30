package q198.operators.decision;

public class TestClass {

    // public static int getSwitch(int x) {
    public static int getSwitch(int x) {
        return x - 20 / x + x * x;
    }

    public static void main(String args[]) {
        switch (getSwitch(10)) {
            case 1:
            case 2:
            case 3:
            default:
                break;
        }
    }

}

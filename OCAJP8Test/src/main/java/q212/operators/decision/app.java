package q212.operators.decision;

public class app {

    public static void main(String[] args) {
        boolean b1 = false;
        boolean b2 = false;
        if (b2 != b1 = !b2) {
            System.out.println("true");
        } else {
            System.out.println("false");
        }
    }

}

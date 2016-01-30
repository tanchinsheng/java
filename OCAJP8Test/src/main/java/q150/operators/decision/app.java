package q150.operators.decision;

public class app {

    public static void main(String[] args) {
        if (8 == 81) {
            System.out.println("true1");
        }
        if (true) {
            System.out.println("true2");
        }

        boolean bool;
        if (bool = false) {
            System.out.println("true3");
        }

        int x = 0;
        if (x == 10 ? true : false) {
            System.out.println("true4");
        } // assume that x is an int

        int y;
        if (y = 3) {
            System.out.println("true5");
        } // assume that x is an int

    }

}

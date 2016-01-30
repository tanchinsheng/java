package q158.operators.decision;

public class CrazyMath {

    public static void main(String[] args) {
        int x = 10, y = 20;
        int dx, dy;
        try {
            dx = x % 5;
            dy = y / dx;
        } catch (ArithmeticException ae) {
            System.out.println("Caught AE");
            dx = 2;
            dy = y / dx;
        }
        x = x / dx;
        y = y / dy;
        System.out.println(dx + " " + dy);
        System.out.println(x + " " + y);
    }
}

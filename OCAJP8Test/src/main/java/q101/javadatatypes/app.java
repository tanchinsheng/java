package q101.javadatatypes;

public class app {

    public static void main(String[] args) {
        int rate = 10;
        int t = 5;
        double amount = 1000.0; // only double
        for (int i = 0; i < t; i++) {
            amount = amount * (1 - rate / 100);
        }
    }

}

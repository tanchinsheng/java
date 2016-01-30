package q262.loop;

public class app {

    public static void main(String[] args) {
        outer:
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 2; j++) {
                System.out.println("1. i=" + i + " , j=" + j);
                if (i == j) {
                    System.out.println("2. i=" + i + " , j=" + j);
                    continue outer;
                }
                System.out.println("3. i=" + i + " , j=" + j);
            }
        }
    }

}

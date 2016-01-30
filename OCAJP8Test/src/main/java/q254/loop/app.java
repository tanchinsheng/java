package q254.loop;

public class app {

    public static void main(String[] args) {
        for (; Math.random() < 0.5;) {
            System.out.println("true");
        }

        for (;; Math.random() < 0.5) {
            System.out.println("true");
        }

        for (;; Math.random()) {
            System.out.println("true");
        }

        for (;;) {
            Math.random() < .05 ? break : continue;
        }

        for (;;) {
            if (Math.random() < .05) {
                break;
            }
        }
    }
}

package q272.loop;

public class BreakTest {

    public static void main(String[] args) {
        int i = 0, j = 5;
        lab1:
        for (;; i++) {
            for (;; --j) {
                if (i > j) {
                    break lab1;//if continue-> infinite loop
                }
            }
        }
        System.out.println(" i = " + i + ", j = " + j);
    }

}

package q157.operators.decision;

public class PromotionTest {

    public static void main(String args[]) {
        int i = 5;
        float f = 5.5f;
        double d = 3.8;
        char c = 'a';
        if (i == f) {
            c++;
        }
        System.out.println((int) f);
        System.out.println((int) d);
        System.out.println(f + d);
        System.out.println((int) (f + d));
        if (((int) (f + d)) == ((int) f + (int) d)) {
            c += 2;
        }
        System.out.println(c);
    }
}

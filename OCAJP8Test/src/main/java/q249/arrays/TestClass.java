package q249.arrays;

public class TestClass {

    public static void incr(int n) {
        n++;
    }

    public static void incr(int[] n) {
        n[0]++;
    }

    public static void main(String[] args) {
        int i = 1;
        int[] iArr = {1};
        incr(i);
        incr(iArr);
        System.out.println("i = " + i + "  iArr[0] = " + iArr[0]); // 1 2
    }

}

package q293.loop;

public class TestClass {

    public static void main(String args[]) {
        int i;
        int j;
        for (i = 0, j = 0; j < i; ++j, i++) {
            System.out.println(i + " " + j);
        }
        System.out.println(i + " " + j);
    }
}

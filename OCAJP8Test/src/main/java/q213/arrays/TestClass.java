package q213.arrays;

public class TestClass {

    public static void main(String[] args) {
        String str = "111";
        boolean[] bA = new boolean[1];
        if (bA[0]) {
            str = "222";
        }
        System.out.println(str);//111

    }

}

package q124.operators.decision;

public class TestClass {

    public static void main(String[] args) {
        // 0,-1,127 -> true
        // -256, 256, 0-255 false
        Integer i = Integer.parseInt(args[0]);
        Integer j = i;
        i--;
        i++;
        System.out.println((i == j));
    }

}

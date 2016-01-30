package q124.operators.decision;

public class app {

    public static void main(String[] args) {
        Integer i = 10;
        Integer j = 10;
        Integer k = new Integer(10);
        System.out.println(i == j);
        System.out.println(i == k); // false
        Byte b = 1;
        Integer c = 1;
        b == c;
    }
}

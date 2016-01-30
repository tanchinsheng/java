package q140.operators.decision;

public class app {

    public static void main(String[] args) {
        Object t = new Integer(106);
        int k = ((Integer) t).intValue() / 10;
        System.out.println(k);

        // Since one of the operands (9.9) is a double,
        //it wil perform a real division and will print 10.1010101010101
        System.out.println(100 / 9.9);

        // Since one of the operands (10.0) is a double,
        //it will perform a real division and will print 10.0
        System.out.println(100 / 10.0);

        System.out.println(100 / 10);

        System.out.println(3 + 100 / 10 * 2 - 13);
    }

}

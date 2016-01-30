package q134.operators.decision;

public class app {

    public static void main(String[] args) {

        Object t = new Integer(107);
        int k1 = (Integer) t.intValue() / 9;
        System.out.println(k1);
        int k2 = ((Integer) t).intValue() / 9;
    }

}

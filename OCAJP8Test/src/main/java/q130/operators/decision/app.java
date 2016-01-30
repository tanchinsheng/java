package q130.operators.decision;

public class app {

    public static void out1() {
        System.out.println("out1");
    }

    public static void out2() {
        System.out.println("out2");
    }

    public static void main(String[] args) {

        int i = 10;
        Long a = new Long(3);
        Integer b = new Integer(1);
        Integer c = new Integer(2);
        System.out.println(i < 20 ? a : b);
        System.out.println(i < 20 ? b : c);
        System.out.println(i < 20 ? null : c);
        System.out.println(i < 20 ? out1() : out2());
        System.out.println(Integer k = i < 20 ? out1() : out2(););

    }

}

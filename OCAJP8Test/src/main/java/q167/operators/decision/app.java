package q167.operators.decision;

public class app {

    public static void main(String[] args) {
        System.out.println(5.5 % 3); // It can be used on floating points operands also. For example, 5.5 % 3 = 2.5
        System.out.println(true & false); // unlike &&, & will not "short circuit" the expression if used on boolean parameters.
        System.out.println(true && false); // !, && and || operate only on booleans.
        System.out.println(~1.123f); // ~ Operates only on integral types
        System.out.println(1 | 2);
        System.out.println(2 && 1);

    }

}

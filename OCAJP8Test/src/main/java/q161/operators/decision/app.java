package q161.operators.decision;

public class app {

    public static void main(String[] args) {
        System.out.println("a" + 'b' + 63); // ab63
        System.out.println("a" + 63); // a63
        System.out.println('b' + new Integer(63)); // 98+63=161
        String s1 = 'b' + 63 + "a"; // 161a
        // Since neither of the operands of + operator is a String, it will not generate a String.
        // However, due to auto-unboxing of 10, it will generate an int value of 73.
        String s2 = 63 + new Integer(10);
    }

}

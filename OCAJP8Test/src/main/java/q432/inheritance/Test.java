package q432.inheritance;

class Super {

    static String ID = "QBANK";

    static String getA() {
        return " A";
    }

}

class Sub extends Super {

    static {
        System.out.print("In Sub");
    }

}

public class Test {

    public static void main(String[] args) {
        System.out.println(Super.ID);
        System.out.println(Sub.ID);// QBANK
        System.out.println(Sub.getA());
        // class Sub is never initialized; the reference to Sub.ID is a reference
        // to a field actually declared in
        // class Super and does not trigger initialization of the class Sub.
    }
}

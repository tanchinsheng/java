package q144.operators.decision;

public class TestClass {

    public static void main(String[] args) {
        Short k = 9;
        Integer i = 9;
        Boolean b = false;
        char c = 'a';
        System.out.println(c);
        String str = "123";

        //choice of =, += and *=
        i = (int) k.shortValue(); // You can use *= here but then you
        // can't complete the 4th line.
        str += b; // You can't use =, or *= here. Only += is valid.
        b = !b; //  You can't use anything other than = here.
        System.out.println(c *= i); //  You can only use *= or +=. = is not valid.
        // Further, if you use += here, you can't complete line 2.
        //c *= i; actually translates to c = (char) c*i;
        //http://docs.oracle.com/javase/specs/jls/se7/html/jls-15.html#jls-15.26.2

    }

}

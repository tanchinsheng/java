/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q432.inheritance;

/**
 *
 * What will be the output when class Test is run?
 */
class Super {

    static String ID = "QBANK";
}

class Sub extends Super {

    static {
        System.out.print("In Sub");
    }
}

public class Test {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println(Sub.ID);// QBANK
        //class Sub is never initialized; the reference to Sub.ID is a reference to a field actually declared in
        // class Super and does not trigger initialization of the class Sub.
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q421.inheritance;

/**
 *
 * @author cstan
 */
interface I1 {

    public default void m1() {
        System.out.println("in I1.m1");
    }
}

interface I2 {

    public default void m1() {
        System.out.println("in I2.m1");
    }
}

class C2 implements I1, I2 { //This class will not compile.

    public void m1() {
        System.out.println("in C2.m1");
    }
}

public class CI implements I1, I2 { //This class will not compile.

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
    }

}

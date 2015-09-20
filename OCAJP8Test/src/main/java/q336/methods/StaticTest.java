/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q336.methods;

/**
 *
 * @author cstan
 */
public class StaticTest {

    /**
     * @param args the command line arguments
     */
    void m1() {
        StaticTest.m2();  // 1
        m4();             // 2
        StaticTest.m3();  // 3
    }

    static void m2() {
    }  // 4

    void m3() {
        m1();            // 5
        m2();            // 6
        StaticTest.m1(); // 7
    }

    static void m4() {
    }

    public static void main(String[] args) {
        // TODO code application logic here
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q412.inheritance;

/**
 *
 * Which of the following statements are true?
 */
class A {

    public static void sM1() {
        System.out.println("In base static");
    }
}

class B extends A {

    /**
     * @param args the command line arguments
     */
    public static void sM1() {
        System.out.println("In sub static");
    } // line1

    public void sM1() {
        System.out.println("In sub non-static");
    } // line2
}

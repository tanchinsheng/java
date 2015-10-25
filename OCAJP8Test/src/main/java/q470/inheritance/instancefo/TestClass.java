/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q470.inheritance.instancefo;

/**
 *
 * Which of the given lines can be inserted at //1 of the following program ?
 */
public class TestClass {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        short s = 9;
        //1

        Integer i = 9;
        System.out.println(s == i);

        Short k = new Short(9);
        System.out.println(k instanceof Short);
    }

}

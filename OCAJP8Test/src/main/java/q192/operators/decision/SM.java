/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q192.operators.decision;

/**
 *
 * What will happen when the following program is compiled and run?
 */
public class SM {

    /**
     * @param args the command line arguments
     */
    public String checkIt(String s) {
        if (s.length() == 0 || s == null) {
            return "EMPTY";
        } else {
            return "NOT EMPTY";
        }
    }

    public static void main(String[] args) {
        SM a = new SM();
        System.out.println(a.checkIt(null));
    }

}

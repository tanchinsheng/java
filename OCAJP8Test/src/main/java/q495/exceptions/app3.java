/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q495.exceptions;

/**
 *
 * @author cstan
 */
public class app3 {

    /**
     * @param args the command line arguments
     */
    Object m1() {
        return new Object();
    }

    void m2() {
        String s = (String) m1();
    }

    public static void main(String[] args) {
        new app3().m2();
    }

}

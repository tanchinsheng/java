/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q473.inheritance.instancefo;

import java.util.Date;

/**
 *
 * @author cstan
 */
public class app {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Object o = null;
        if (o instanceof Date) {
            System.out.println("true");
        }
        String s = null;
        if (s instanceof Date) {
            System.out.println("true");
        }

    }

}

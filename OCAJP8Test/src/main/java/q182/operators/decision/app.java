/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q182.operators.decision;

/**
 *
 * @author cstan
 */
public class app {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Integer i1 = 1;
        Integer i2 = new Integer(1);
        int i3 = 1;
        Byte b1 = 1;
        Long g1 = 1L;

        if (i1 == i2) {
            System.out.println("true1");
        } else {
            System.out.println("false1");
        }
        if (i1 == i3) {
            System.out.println("true2");
        } else {
            System.out.println("false2");
        }

        if (i1.equals(i2)) {
            System.out.println("true4");
        } else {
            System.out.println("false4");
        }
        if (i1.equals(g1)) {
            System.out.println("true5");
        } else {
            System.out.println("false5");
        }
        if (i1.equals(b1)) {
            System.out.println("true6");
        } else {
            System.out.println("false6");
        }

        if (i1 == b1) {
            System.out.println("true3");
        }
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q106.javadatatypes;

/**
 *
 * @author cstan
 */
public class app {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Integer a = new Integer(1);
        Integer b = new Integer(2);
        Integer c = new Integer(3);

        if (a.equals(a)) {
            System.out.println("true1");
        }
        if (b.equals(c)) {
            System.out.println("true2");
        }
        // The wrapper classes's equals() method overrides Object's equals() method
        // to compare the actual value instead of the reference.
        if (a.equals(b)) {
            System.out.println("true3");
        }
    }

}

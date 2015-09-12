/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q138.operators.decision;

/**
 *
 * @author cstan
 */
public class app {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // int m = 1; // m can hold all the case values.
        // long m;  // long, float, double, and boolean can never be used as a switch variable.
        // char m; // m can hold all the case values.
        // byte m; // m will not be able to hold 128. a byte's range is -128 to 127.
        // short m; // m can hold all the case values.
        //other code   
        switch (m) {
            case 32:
                System.out.println("32");
                break;
            case 64:
                System.out.println("64");
                break;
            case 128:
                System.out.println("128");
                break;
        }
    }

}

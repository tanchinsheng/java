/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q186.operators.decision;

/**
 *
 * @author cstan
 */
public class app {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        byte b = 1;  // this is integer
        char c = 1;
        short s = 1;
        int i = 1;

        s = b * b; // b * b returns an int.
        i = b + b;
        s *= b; // All compound assignment operators internally do an explicit cast.
        c = c + b; // c + b returns an int
        s += i; // All compound assignment operators internally do an explicit cast.
    }

}

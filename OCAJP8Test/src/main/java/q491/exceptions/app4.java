/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q491.exceptions;

/**
 *
 * @author cstan
 */
public class app4 {

    /**
     * @param args the command line arguments
     */
    static {
        if (true) {
            throw new NullPointerException();//ExceptionInInitializerError
        }
    }

    public static void main(String[] args) {
        // TODO code application logic here
    }

}

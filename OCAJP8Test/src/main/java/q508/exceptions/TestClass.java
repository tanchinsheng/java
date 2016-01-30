/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q508.exceptions;

/**
 *
 * @author cstan
 */
public class TestClass {

    /**
     * @param args the command line arguments
     */
    public static void methodX() throws Exception {
        throw new AssertionError();
    }

    public static void main(String[] args) {
        try {
            methodX();
        } catch (Exception e) {
            System.out.println("EXCEPTION");
        }
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q326.methods;

/**
 *
 * @author cstan
 */
public class TestClass {

    /**
     * It is not possible to access x from main without making it static.
     * Because main is a static method and only static members are accessible
     * from static methods. There is no 'this' available in main so none of the
     * this.x are valid.
     */
    int x;

    public static void main(String[] args) {
        // lot of code.
    }

}

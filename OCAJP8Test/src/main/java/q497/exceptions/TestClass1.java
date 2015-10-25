/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q497.exceptions;

/**
 *
 * @author cstan
 */
public class TestClass1 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        B b = new A(); //B b = new A(); is not valid because a superclass object can never be assigned to a base class reference.
        b.m();
    }

}

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
public class TestClass4 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Object o = new B();
        o.m(); //Object class does not have method m(). So o.m() will not compile. You can do ( (B) o ).m();
    }

}

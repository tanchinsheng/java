/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany;

/**
 *
 * @author cstan
 */
public class PassByValue {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Dog aDog = new Dog("Max");
        foo(aDog);
        if (aDog.getName().equals("Max")) { //true
            System.out.println("Java passes by value.");
        } else if (aDog.getName().equals("Fifi")) {
            System.out.println("Java passes by reference.");
        }
    }

    public static void foo(Dog d) {
        d.getName().equals("Max"); // true
        d = new Dog("Fifi");
        d.getName().equals("Fifi"); // true
    }

}

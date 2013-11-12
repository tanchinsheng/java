/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package part2usingvariables;

/**
we look at declaring and initializing variables — 
* one of the most basic building blocks of any computer program
 */
public class App {
    public static void main(String[] args) {
        Number myIntNumber = 1234567;
        
        int myInt = 88;
        short myShort = 847;
        long myLong = 9797;
        
        Number myDoubleNumber = 1234567.123;
        double myDouble = 7.3243;
        float myFloat = 324.3f;
        
        char myChar = 'y';
        boolean  myBoolean = false;
        byte myByte = 127;
        
        System.out.println(myIntNumber);
        System.out.println(myInt);
        System.out.println(myIntNumber.intValue() + myInt);
        System.out.println(myIntNumber.intValue() + myFloat);
        System.out.println(myShort);
        System.out.println(myLong);
        System.out.println(myDouble);
        System.out.println(myDoubleNumber.doubleValue() + myDouble);
        System.out.println(myDoubleNumber.doubleValue() + myFloat);// ok
        System.out.println(myFloat);
        System.out.println(myChar);
        System.out.println(myBoolean);
        System.out.println(myByte);
        
    }
}

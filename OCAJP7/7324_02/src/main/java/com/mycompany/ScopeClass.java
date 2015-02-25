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
public class ScopeClass {

    private int i = 35;

    public static void main(String argv[]) {
        int i = 45;
        ScopeClass s = new ScopeClass();
        s.someMethod();
    }

    public static void someMethod() {
        // You cannot access an instance variable from a static method.
        //System.out.println(i);

    }

    public void anotherMethod() {
        //char d="d";
        char d = 'd';
        //float f=3.1415;
        float f = 3.1415f;
        int i = 34;
        //byte b = 257;
        byte b = (byte) 257; //-128 to +127
        boolean isPresent = true;
    }

}

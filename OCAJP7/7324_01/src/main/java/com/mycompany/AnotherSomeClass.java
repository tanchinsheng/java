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
public class AnotherSomeClass {

    public int i;

    public static void main(String argv[]) {
        AnotherSomeClass sc = new AnotherSomeClass();
// Comment line
        sc.i = 5;
        int j = sc.i;
        //sc.i = 5.0;
        System.out.println(sc.i);
    }
}

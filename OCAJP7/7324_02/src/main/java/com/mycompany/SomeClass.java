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
public class SomeClass {

    public int i;

    public static void main(String argv[]) {
        SomeClass sc = new SomeClass();
// Comment line
        System.out.println(i);
        System.out.println(sc.i);
        System.out.println(SomeClass.i);
        System.out.println((new SomeClass()).i);

        StringBuilder sb = new StringBuilder();
        sb.append(34.5);
        sb.deleteCharAt(34.5);
        sb.toInteger(3);
        sb.toString();

        String s = "banana";
        int i;
        //s.lastIndexOf(2,s);
        s.lastIndexOf(s, 2);
        s.indexOf('a'); // correct
        s.charAt(2);
        s.indexOf(s, 'v');

    }
}

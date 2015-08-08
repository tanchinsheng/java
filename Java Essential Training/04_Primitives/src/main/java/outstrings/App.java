/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package outstrings;

import java.util.Date;

/**
 *
 * @author cstan
 */
public class App {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        char c = 'z';
        boolean bool = true;
        byte b = 127;
        short s = 32000;
        int i = 2000000;
        long l = 10000000L;
        float f = 1234245.435234f;
        double d = 112312312331.34;

        //automatically translate to String
        System.out.println(c);
        System.out.println(bool);
        System.out.println(b);
        System.out.println(s);
        System.out.println(i);
        System.out.println(l);
        System.out.println(f);
        System.out.println(d);

        System.out.println("The value of s is " + s);
        System.out.println(s + s + " The value of s is ");

        Date myDate = new Date();
        System.out.println("New date is " + myDate);
    }

}

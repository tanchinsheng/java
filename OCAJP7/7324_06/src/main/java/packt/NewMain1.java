/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package packt;

/**
 *
 * @author cstan
 */
public class NewMain1 {

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        String s = "string 1";
        int i = 5;
        someMethod1(i);
        System.out.println(i);
        someMethod2(s);
        System.out.println(s);
    }

    public static void someMethod1(int i) {
        System.out.println(++i);
    }

    public static void someMethod2(String s) {
        s = "string 2";
        System.out.println(s);
    }

}

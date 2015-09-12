/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q174.operators.decision;

/**
 *
 * Which of the following lines of code may throw a NullPointerException in
 * certain situations?
 */
public class app {

    /**
     * If s is null, s.length would throws NullPointerException
     */
    public static void main(String[] args) {
        int i = 0;
        String s = "";

        //s = null;
        if ((s != null) | (i == s.length())) {
        } //(i == str.length()) will always be executed so if 'str' is null, then str.length() will throw a NullPointerException.
        System.out.println("A");

        //s = null;       
        if ((s == null) | (i == s.length())) {
        } // (i == str.length()) will always be executed so if 'str' is null, then str.length() will throw a NullPointerException.
        System.out.println("B");

        //s = null;       
        if ((s != null) || (i == s.length())) {
        } // (i == str.length()) will only be evaluated if (str != null) is false, and (str != null) will be false if 'str' is null.
        //So it will also throw a NullPointerException.
        System.out.println("C");
        s = null;
        if ((s == null) || (i == s.length())) {
        } // (i == str.length()) will only be evaluated if (str == null) is false, and (str == null) will be false if 'str' is NOT null.
        // So it will NEVER throw a NullPointerException.
        System.out.println("D");
    }

}

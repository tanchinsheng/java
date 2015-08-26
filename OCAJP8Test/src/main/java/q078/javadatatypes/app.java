/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q078.javadatatypes;

/**
 *
 * @author cstan
 */
public class app {

    /**
     * In all of these cases, auto-unboxing of integers will occur. For the last
     * statement, after unboxing a and b, the value 12 will be boxed into an
     * Integer object.
     */
    public static void main(String[] args) {
        int a = 5, b = 7, k = 0;
        Integer m = null;
        k = new Integer(a) + new Integer(b);
        System.out.println(k);
        k = new Integer(a) + b;
        System.out.println(k);
        k = b + new Integer(a);
        System.out.println(k);
        m = new Integer(a) + new Integer(b);
        System.out.println(m);
    }

}

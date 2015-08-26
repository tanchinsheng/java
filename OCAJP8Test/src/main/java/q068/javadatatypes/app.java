/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q068.javadatatypes;

/**
 *
 * @author cstan
 */
public class app {

    /**
     * In all of these statements, auto-unboxing of integers will occur. For the
     * last statement, after unboxing a and b, the value 12 will be boxed into
     * an Integer object.
     */
    public static void main(String[] args) {
        int a = 5, b = 7, k1 = 0, k2 = 0, k3 = 0;
        Integer m = null;

        k1 = new Integer(a) + new Integer(b);//1
        System.out.println(k1);
        k2 = new Integer(a) + b; //2        
        System.out.println(k2);
        k3 = a + new Integer(b); //3
        System.out.println(k3);
        m = new Integer(a) + new Integer(b); //4
        System.out.println(m);
    }

}

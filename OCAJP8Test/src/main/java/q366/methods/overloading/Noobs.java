/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q366.methods.overloading;

/**
 *
 * What will the following code print when run?
 */
public class Noobs {

    /**
     * It looks confusing but it is a simple question. Remember that whenever
     * two methods are applicable for a method call, the one that is most
     * specific to the argument is chosen. In case of m(a), a is an int, which
     * cannot be passed as a char (because an int cannot fit into a char).
     * Therefore, only m(int) is applicable. In case of m(c), c is a char, which
     * can be passed as an int as well as a char. Therefore, both the methods
     * are applicable. However, m(char) is most specific therefore that is
     * chosen over m(int).
     */
    public void m(int a) {
        System.out.println("In int ");
    }

    public void m(char c) {
        System.out.println("In char ");
    }

    public static void main(String[] args) {

        Noobs n = new Noobs();
        int a = 'a';
        char c = 6;
        n.m(a);
        n.m(c);
    }

}

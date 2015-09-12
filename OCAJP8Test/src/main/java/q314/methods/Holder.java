/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q314.methods;

/**
 *
 * What will the following class print when compiled and run?
 */
public class Holder {

    /**
     * @param args the command line arguments
     */
    int value = 1;
    Holder link;

    public Holder(int val) {
        this.value = val;
    }

    public static void main(String[] args) {
        final Holder a = new Holder(5);
        Holder b = new Holder(10);
        a.link = b;

        b.link = setIt(a, b);
        System.out.println(a.link.value + " " + b.link.value);
    }

    public static Holder setIt(final Holder x, final Holder y) {
        x.link = y.link;
        return x;
    }

}

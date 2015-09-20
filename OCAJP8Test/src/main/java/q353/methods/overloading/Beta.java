/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q353.methods.overloading;

/**
 *
 * What will the following code print when run?
 */
class Baap {

    public int h = 4;

    public int getH() {
        System.out.println("Baap " + h);
        return h;
    }
}

public class Beta extends Baap {

    /**
     * Always remember: Methods are overridden and variables are shadowed. Here,
     * b refers to an object of class Beta so b.getH() will always call the
     * overridden (subclass's method). However, the type of reference of b is
     * Baap. so b.h will always refer to Baap's h. Further, inside Beta's
     * getH(), Beta's h will be accessed instead of Baap's h because you are
     * accessing this.h ('this' is implicit) and the type of this is Beta.
     */
    public int h = 44;

    @Override
    public int getH() {
        System.out.println("Beta " + h);
        return h;
    }

    public static void main(String[] args) {
        Baap b = new Beta();
        System.out.println(b.h + " " + b.getH());
        Beta bb = (Beta) b;
        System.out.println(bb.h + " " + bb.getH());
    }

}

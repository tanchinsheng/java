/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q343.methods;

/**
 *
 * Given the following definition of class, which member variables are
 * accessible from OUTSIDE the package q343.methods?
 */
public class TestClass {

    /**
     * Member variable k, but only for subclasses.
     */
    int i;
    public int j;
    protected int k;
    private int l;

    public static void main(String[] args) {
        // protected things (methods and fields) can be accessed from within the package and from subclasses
    }

}

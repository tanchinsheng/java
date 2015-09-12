/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q256.loop;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 *
 * Identify the valid for loop constructs assuming the following declarations:
 */
public class app {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Object o = null;
        Collection c = new ArrayList();//valid collection object
        // for(o : c){ }
        // Cannot use an existing/predefined variable in the variable declaration part.
        for (final Object o2 : c) {
        }
        // final is the only modifier (excluding annotations) that is allowed here.

        // for(Iterator it : c.iterator()){ }
        // c.iterator() does not return any Collection.
        // Note that the following would have been valid:
        // Collection<Iterator> c = //some collection that contains Iterator objects for(Iterator it : c){ }
        int[][] ia = {{1}, {1}};//valid array
        // for(int i : ia) { }
        // Each element of ia is itself an array. Thus, they cannot be assigned to an int.

        for (int i : ia[0]) {
        }
        // Since ia[0] is an array of ints, this is valid.
        // (It may throw a NullPointerException or ArrayIndexOutOfBoundsException at runtime
        // if ia is not appropriately initialized.)
    }

}

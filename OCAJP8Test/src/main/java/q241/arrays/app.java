/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q241.arrays;

import java.util.ArrayList;

/**
 *
 * Which of the following are valid code fragments:
 */
public class app {

    /**
     * 1. An array of objects can store Objects of any class.
     *
     * 2. Primitives (i.e. int, byte, char, short, boolean, long, double, and
     * float) are NOT objects.
     *
     * 3. An array (of primitives as well as of objects) is an Object.
     */
    public static void main(String[] args) {
        Object o1 = new Object[]{"aaa", new Object(), new ArrayList(), 10};
        // 10 is a primitive and not an Object but due to auto-boxing it will be converted into an Integer
        // object and that object will then be stored into the array of Objects.
        Object o2 = new Object[]{"aaa", new Object(), new ArrayList(), {}};
        // {} is not a valid way to create an Object here.
        // However, it is valid while creating an array.
        // For example, the following are valid:
        // String[] sa = { }; //assigns a valid String[] object of length 0 to sa
        // Object arr[][] = new Object[][] {new String[5], {} }; //assigns a valid Object[] object of length 0 to arr[1].
        Object o3 = new Object[]{"aaa", new Object(), new ArrayList(), new String[]{""}};
        //Every array is an Object so new String[]{""} is also an Object and can be placed in an array of objects.
        Object o4 = new Object[1]
        {
            new Object()
        }; //You can't specify array length if you are initializing it at the same place.
    }

}

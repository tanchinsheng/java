/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q235.arrays;

/**
 *
 * Given the following declaration, select the correct way to get the number of
 * elements in the array, assuming that the array has been initialized.
 */
public class app {

    /**
     * FYI, All types of arrays are objects. i.e. intArr instanceof Object is
     * true.
     */
    public static void main(String[] args) {
        int[] intArr = {1};
        int l = intArr.length;
        // Each array object has a member variable named public final length of type 'int' that contains the size of the array.
    }

}

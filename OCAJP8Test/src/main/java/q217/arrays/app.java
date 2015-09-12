/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q217.arrays;

/**
 *
 * What will the following code snippet print
 */
public class app {

    /**
     * When you create an array of Objects ( here, Strings) all the elements are
     * initialized to null. So in the line 3, null is assigned to myStr. Note
     * that. empty string is "" ( String str = ""; ) and is not same as null.
     */
    public static void main(String[] args) {
        int index = 1;
        String[] strArr = new String[5];
        String myStr = strArr[index];
        System.out.println(myStr); //null
    }

}

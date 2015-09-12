/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q245.arrays;

/**
 *
 * What will be the result of attempting to run the following program?
 */
public class StringArrayTest {

    /**
     * arr[0][1][2] => [0] = { { "a", "b" , "c"}, { "d", "e", null } }, [1] = {
     * "d", "e", null } and [2] = null. So it will print null.
     */
    public static void main(String[] args) {
        String[][][] arr = {{{"a", "b", "c"},
        {"d", "e", null}},
        {{"x"}, null}, {{"y"}},
        {{"z", "p"}, {}}};
        System.out.println(arr[0][1][2]);
    }

}

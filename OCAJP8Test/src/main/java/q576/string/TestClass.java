/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q576.string;

/**
 *
 * What will be the result of attempting to compile and run the following code?
 */
public class TestClass {

    /**
     * Note that String objects are immutable. No matter what operation you do,
     * the original object will remain the same and a new object will be
     * returned. Here, the statement str1.concat(str2) creates a new String
     * object which is printed but its reference is lost after the printing.
     */
    public static void main(String[] args) {
        String str1 = "str1";
        String str2 = "str2";
        System.out.println(str1.concat(str2));//str1str2
        System.out.println(str1);//str
        //str1.concat(str2) actually creates a new object that contains "str1str2".
        //So it does not affect the object referenced by str1.

    }

}

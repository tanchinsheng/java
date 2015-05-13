/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q33;

public class NullAccess {

    public static void main(String[] args) {
        String str = null;
        //Calling a static method does not require dereferencing the reference variable
        System.out.println(str.valueOf(10));
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q010.javabasics;

/**
 *
 * java and classname are not part of the args array. So tom gets "111", dick
 * gets "222" and harry gets "333".java and classname are not part of the args
 * array. So tom gets "111", dick gets "222" and harry gets "333".
 */
public class TestClass {

    public static void main(String[] args) {
        String tom = args[0];
        String dick = args[1];
        String harry = args[2];
        System.out.println(harry);
    }
}

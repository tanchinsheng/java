/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter5;

public class Panda1 extends Bear1 {

    public void sneeze() { // DOES NOT COMPILE
        System.out.println("Panda bear sneezes quietly");
    }

    public static void hibernate() { // DOES NOT COMPILE
        System.out.println("Panda bear is going to sleep");
    }
}

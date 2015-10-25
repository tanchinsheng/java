/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q443.inheritance;

/**
 *
 * What will the program print when compiled and run?
 */
public class Type1Bozo implements Bozo {

    /**
     * Fields defined in an interface are ALWAYS considered as public, static,
     * and final. Even if you don't explicitly define them as such. In fact, you
     * cannot even declare a field to be private or protected in an interface.
     * Therefore, you cannot assign any value to 'type' outside the interface
     * definition.
     */
    public Type1Bozo() {
        type = 1;
    }

    public void jump() {
        System.out.println("jumping..." + type);
    }

    public static void main(String[] args) {
        Bozo b = new Type1Bozo();
        b.jump();
    }

}

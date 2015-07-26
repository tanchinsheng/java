/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FunctionalInterface;

import FunctionalInterface.interfaces.InterfaceWithArgs;

/**
 *
 * @author cstan
 */
public class UseInterfaceWithArgs {

    public static void main(String[] args) {
        InterfaceWithArgs obj = (v1, v2) -> {
            int result = v1 * v2;
            System.out.println("The result is " + result);
        };
        obj.calculate(10, 5);
    }
}

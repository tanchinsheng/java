/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FunctionalInterface;

import FunctionalInterface.interfaces.SimpleInterface;

/**
 *
 * @author cstan
 */
public class UseSimpleInterface {

    public static void main(String[] args) {
        SimpleInterface obj = () -> System.out.println("Say something");
        obj.doSomething();
    }
}

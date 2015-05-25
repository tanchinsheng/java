/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces.evolve;

/**
 *
 * @author cstan
 */
public interface DoItWithDefaultMethod {

    void doSomething(int i, double x);

    int doSomethingElse(String s);

    // only java 8
    default boolean didItWork(int i, double x, String s) {
        // Method body
        return true;
    }
}

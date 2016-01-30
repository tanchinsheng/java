/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q530.exceptions;

/**
 *
 * What will the following code print when compiled and run?
 */
abstract class Calculator {

    abstract void calculate();

    public static void main(String[] args) {
        System.out.println("calculating");
        Calculator x = null;
        x.calculate(); //After printing, when it tries to call calculate() on x, it will throw NullPointerException since x is null.
    }
}

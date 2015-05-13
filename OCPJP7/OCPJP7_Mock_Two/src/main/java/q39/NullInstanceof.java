/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q39;

import java.util.concurrent.atomic.AtomicInteger;

public class NullInstanceof {

    // It is not a compiler error to check null with the instanceof operator
    // However, if null is passed for the instanceof operator, returns false.
    public static void main(String[] args) {
        if (null instanceof Object) {
            System.out.println("null is instance of Object");
        }
        if (null instanceof AtomicInteger) {
            System.out.println("null is instance of AtomicInteger");
        }
    }

}

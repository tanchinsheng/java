/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q38;

import java.util.concurrent.atomic.AtomicInteger;

public class Increment {

    public static void main(String[] args) {
        AtomicInteger i = new AtomicInteger(0);
        increment(i);
        System.out.println(i);
    }

    static void increment(AtomicInteger atomicInt) {
        atomicInt.incrementAndGet();
    }
}

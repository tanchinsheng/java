/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q54;

import java.util.concurrent.atomic.AtomicInteger;

public class AtomicVariableTest {

    private static final AtomicInteger counter = new AtomicInteger(0);

    static class Decrementer extends Thread {

        @Override
        public void run() {
            counter.decrementAndGet();
            System.out.println("Decrementer's counter : " + counter);
        }
    }

    static class Incrementer extends Thread {

        @Override
        public void run() {
            counter.incrementAndGet();
            System.out.println("Incrementer's counter : " + counter);
        }
    }

    public static void main(String[] args) {
        for (int i = 0; i < 5; i++) {
            new Incrementer().start();
            new Decrementer().start();
        }

        System.out.println(counter);
    }

}

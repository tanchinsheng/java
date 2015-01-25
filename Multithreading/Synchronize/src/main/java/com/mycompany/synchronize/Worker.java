/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.synchronize;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author cstan
 */
public class Worker {

    private int count = 0;

    public void run() {
        Thread thread1 = new Thread(new Runnable() {

            public void run() {
                for (int i = 0; i < 10000; i++) {
                    count++;
                }
            }
        });
        thread1.start();

        Thread thread2 = new Thread(new Runnable() {

            public void run() {
                for (int i = 0; i < 10000; i++) {
                    count++;
                }
            }
        });
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(Worker.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println("Count is: " + count);

    }
}

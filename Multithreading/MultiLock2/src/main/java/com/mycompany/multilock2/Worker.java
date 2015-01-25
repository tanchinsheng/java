/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.multilock2;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author cstan
 * 
 * Suppose, for example, class MsLunch has two instance fields, c1 and c2, 
 * that are never used together. 
 * All updates of these fields must be synchronized, 
 * but there's no reason to prevent an update of c1 from being interleaved 
 * with an update of c2 — and doing so 
 * reduces concurrency by creating unnecessary blocking. 
 * Instead of using synchronized methods or otherwise 
 * using the lock associated with this, 
 * we create two objects solely to provide locks.
 */
public class Worker {

    final Object lock1 = new Object();
    final Object lock2 = new Object();

    public void stageOne() {
        synchronized (lock1) {
            sleep();// make sure not writing to same data
        }
    }

    public void stageTwo() {
        synchronized (lock2) {
            sleep();// make sure not writing to same data
        }
    }

    public void sleep() {
        try {
            Thread.sleep(5);
        } catch (InterruptedException ex) {
            Logger.getLogger(Worker.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void process() {
        for (int i = 0; i < 500; i++) {
            stageOne();
            stageTwo();
        }
    }

    public void run() {

        long start = System.currentTimeMillis();

        Thread t1 = new Thread(new Runnable() {

            public void run() {
                process();
            }
        });

        Thread t2 = new Thread(new Runnable() {

            public void run() {
                process();
            }
        });

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(Worker.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println("Time taken: " + (System.currentTimeMillis() - start));
    }
}

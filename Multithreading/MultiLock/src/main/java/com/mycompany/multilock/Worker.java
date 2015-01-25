/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.multilock;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author cstan
 */
public class Worker {

    //synchronized methods
    public synchronized void stageOne() {
        sleep();

    }

    public synchronized void stageTwo() {
        sleep();

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

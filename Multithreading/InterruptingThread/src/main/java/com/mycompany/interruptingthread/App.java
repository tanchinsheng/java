package com.mycompany.interruptingthread;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Hello world!
 *
 */
public class App {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Starting.");
        Thread t = new Thread(new Runnable() {

            @Override
            public void run() {
                Random ran = new Random();
                for (int i = 0; i < 1E8; i++) {
                     Math.sin(ran.nextDouble());
                }
            }
            
        });
        t.start();
        Thread.sleep(500);
        t.interrupt();
        t.join();
        System.out.println("Finished.");
    }
}

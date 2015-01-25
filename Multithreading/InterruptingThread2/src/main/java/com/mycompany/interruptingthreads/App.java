package com.mycompany.interruptingthreads;

import java.util.Random;

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
                    if (Thread.currentThread().isInterrupted()) {
                        System.out.println("Interrupted!");
                        break;
                    }
                    Math.sin(ran.nextDouble());
                }
            }
        });
        t.start();
        Thread.sleep(500);
        t.interrupt();//set flag in thread
        t.join();
        System.out.println("Finished.");
    }
}

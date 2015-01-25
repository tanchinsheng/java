package com.mycompany.producerconsumer;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Hello world!
 *
 */
public class App {

    private static BlockingQueue<Integer> queue = new ArrayBlockingQueue<Integer>(10);

    public static void main(String[] args) throws InterruptedException {

        Thread t1 = new Thread(new Runnable() {

            public void run() {
                producer();
            }
        });

        Thread t2 = new Thread(new Runnable() {

            public void run() {
                consumer();
            }
        });

        t1.start();
        t2.start();
        
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void producer() {
        Random random = new Random();
        while (true) {
            try {
                queue.put(random.nextInt(100));
            } catch (InterruptedException ex) {
                Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private static void consumer() {
        Random random = new Random();
        while (true) {
            try {
                Thread.sleep(100);//try to use wait!
                if (random.nextInt(10) == 0) {//generated random number is 0.
                    Integer value = queue.take();
                    System.out.println("Taken value is " + value + "; Queue size is " + queue.size());
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}

package demo1;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
In this tutorial we look at using the synchronized keyword to coordinate actions between threads 
* and prevent them screwing up each other’s work.
 */
public class App {

    private int count = 0;

    public static void main(String[] args) {
        App app = new App();
        app.doWork();
    }
    // acquire interstinic lock of object
    public synchronized void increment() {
        count++;
    }

    public void doWork() {
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10000; i++) {
                    //count++;
                    increment();
                }
            }
        });
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10000; i++) {
                    //count++;
                    increment();
                }
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
        System.out.println("count: " + count);
    }

}

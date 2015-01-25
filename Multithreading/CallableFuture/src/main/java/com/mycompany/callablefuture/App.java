package com.mycompany.callablefuture;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Hello world!
 *
 */
public class App {

    public static void main(String[] args) {
        //System.out.println( "Hello World!" );
        ExecutorService executor = Executors.newCachedThreadPool();
        executor.submit(new Runnable() {

            public void run() {
                int duration = (new Random()).nextInt(4000);
                System.out.println("Starting...");
                try {
                    Thread.sleep(duration);
                } catch (InterruptedException ex) {
                    Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
                }
                 System.out.println("Finished.");
            }
        });
        executor.shutdown();
    }
}

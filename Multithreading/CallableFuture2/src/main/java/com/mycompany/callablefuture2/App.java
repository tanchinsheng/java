package com.mycompany.callablefuture2;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.*;
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
        Future<Integer> future = executor.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                int duration = (new Random()).nextInt(4000);
                if (duration > 2000){
                    throw new IOException("Sleeping for too long.");
                }
                System.out.println("Starting...");
                try {
                    Thread.sleep(duration);
                } catch (InterruptedException ex) {
                    Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
                }
                System.out.println("Finished.");
                return duration;
            }
        });
        executor.shutdown();
        
        
        //executor.awaitTermination(timeout, TimeUnit.DAYS);
        try {
            System.out.println("Result is:" + future.get());
        } catch (InterruptedException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ExecutionException ex) {
            IOException e = (IOException) ex.getCause();
            System.out.println("ExecutionException :" + e.getMessage());
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

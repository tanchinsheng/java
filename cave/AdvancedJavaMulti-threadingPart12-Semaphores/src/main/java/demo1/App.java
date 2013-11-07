package demo1;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

//A tutorial on semaphores in Java. Semaphores are mainly used to limit the number of simultaneous threads 
//that can access a resources, but you can also use them to implement deadlock recovery systems since 
//a semaphore with one permit is basically a lock that you can unlock from other threads. 
//In this video we’ll take a look at the most important methods of the Semaphore class, 
//and I’ll also show you an example of limiting the number of “connections” that threads can make simultaneously.

public class App {
 
    public static void main(String[] args) throws Exception {
         
// creates 200 threads and fires them off simultaneously. 
// They all try to run the connect() method of the Connection class at the same time.       
        
        ExecutorService executor = Executors.newCachedThreadPool();      
        for(int i=0; i < 200; i++) {
            executor.submit(new Runnable() {
                public void run() {
                    Connection.getInstance().connect();
                }
            });
        }     
        executor.shutdown();       
        executor.awaitTermination(1, TimeUnit.DAYS);
    }
 
}
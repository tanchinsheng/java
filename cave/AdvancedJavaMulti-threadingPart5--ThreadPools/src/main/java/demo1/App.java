package demo1;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

// manage multiple threads in Java using thread pools. 
// With thread pools you can assign a whole gaggle of threads to work through your queue of tasks.

class Processor implements Runnable {
    private final int id;
    public Processor(int id) {
        this.id = id;
    }    
    
    @Override
    public void run() {
        System.out.println ("Starting.... " + id);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException ex) {
            Logger.getLogger(Processor.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println ("Completed.... " + id);
    }
}
        
public class App 
{
    public static void main( String[] args )
    {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        for (int i =0; i<5; i++) {
            executor.submit(new Processor(i));
        } 
        executor.shutdown();
        System.out.println( "All tasks submitted." );
        try {
            executor.awaitTermination(1, TimeUnit.DAYS);
        } catch (InterruptedException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println( "All tasks completed." );
    }
}

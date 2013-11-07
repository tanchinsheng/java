package demo1;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

//using the excellent CountDownLatch Java class to synchronize your threads’ activities.

class Processor implements Runnable {

    private final CountDownLatch latch;

    public Processor(CountDownLatch latch) {
        this.latch = latch;
    }

    public void run() {
        System.out.println("started...latch - " + latch.getCount());
        try {
            Thread.sleep(3000);
        } catch (InterruptedException ex) {
            Logger.getLogger(Processor.class.getName()).log(Level.SEVERE, null, ex);
        }
        latch.countDown();
    }
}

public class App {

    public static void main(String[] args) {
        CountDownLatch latch = new CountDownLatch(3);
        ExecutorService executor = Executors.newFixedThreadPool(3);
        for (int i = 0; i < 3; i++) {
            executor.submit(new Processor(latch));
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Processor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        try {
            latch.await();
        } catch (InterruptedException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Completed");
    }
}

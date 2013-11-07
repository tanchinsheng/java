package demo1;

/**
 * How to use Callable and Future in Java to get results from your threads and
 * to allow your threads to throw exceptions. Plus, Future allows you to control
 * your threads, checking to see if they’re running or not, waiting for results
 * and even interrupting them or descheduling them.
 */
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class App {

    public static void main(String[] args) {
        ExecutorService executor = Executors.newCachedThreadPool();

        //Future<?> future = executor.submit(new Callable<Void>() {
        Future<Integer> future = executor.submit(new Callable<Integer>() {

            @Override
            //public Void call() throws Exception {
            public Integer call() throws Exception {
                Random random = new Random();
                int duration = random.nextInt(4000);

                if (duration > 2000) {
                    throw new IOException("Sleeping for too long.");// throws from call
                }

                System.out.println("Starting ...");

                try {
                    Thread.sleep(duration);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                System.out.println("Finished.");

                return duration;
                //return null;
            }

        });

        executor.shutdown();
       // future.cancel(true);
        //future.isDone();
        try {
            System.out.println("Result is: " + future.get());//no need awaitTerminsation
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            IOException ex = (IOException) e.getCause();

            System.out.println(ex.getMessage());
        }
    }

}

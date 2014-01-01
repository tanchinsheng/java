package objectscope;

public class NarrowObjectScope {

    public static void main(String[] args) throws InterruptedException {
        
        Thread.sleep(1000);
        long t1 = System.nanoTime();
        for (int i = 0; i < 500000000; i++) {
            String narrowScope = "" + i;
        }
        long t2 = System.nanoTime();
        System.out.println("Execution time: " + ((t2 - t1) * 1e-6) + " milliseconds");
    }
}

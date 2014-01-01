package StringClazz;

public class NewStringClazz {

    public static void main(String[] args) throws InterruptedException {

        Thread.sleep(1000);
        long t1 = System.nanoTime();
        for (int i = 0; i < 2000000000; i++) {
//Looping 10M rounds
            String x = new String("Hello" + "," +" "+ "World");
        }
        long t2 = System.nanoTime();
        System.out.println("Execution time: " + ((t2 - t1) * 1e-6) + " milliseconds");
    }
}

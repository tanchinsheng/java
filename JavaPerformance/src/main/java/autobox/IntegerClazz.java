package autobox;

public class IntegerClazz {

    public static void main(String[] args) throws InterruptedException {

        Thread.sleep(1000);
        long t1 = System.nanoTime();
        Integer i = 0;
        //Counting by 10M
        while (i < 100000000) {
            i++;
        }
        long t2 = System.nanoTime();
        System.out.println("Execution time: " + ((t2 - t1) * 1e-6) + " milliseconds");
    }
}

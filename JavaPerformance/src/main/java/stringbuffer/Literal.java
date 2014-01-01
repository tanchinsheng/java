package stringbuffer;

public class Literal {

    public static void main(String[] args) throws InterruptedException {

        Thread.sleep(1000);
        long t1 = System.nanoTime();
        String name = "Smith";
        for (int i = 0; i < 200000000; i++) {
//Looping 1M rounds
            String x = "Hello";
            x += ",";
            x += " Mr.";
            x += name;
        }
        long t2 = System.nanoTime();
        System.out.println("Execution time: " + ((t2 - t1) * 1e-6) + " milliseconds");
    }
}

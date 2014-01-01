package stringempty;

public class PreCheckLength {

    public static void main(String[] args) throws InterruptedException {

        //Thread.sleep(1000);
        long t1 = System.nanoTime();
        String a = "test";
        int length = a.length();
        for (int i = 0; i < 500000; i++) {
//10m loops
            if (a != null && length == 0) {
                
            }
            System.out.println("i = " + i );
        }
        long t2 = System.nanoTime();
        System.out.println("Execution time: " + ((t2 - t1) * 1e-6) + " milliseconds");
        //Execution time: 353.090706 milliseconds
    }
}

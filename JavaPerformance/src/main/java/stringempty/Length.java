package stringempty;

import stringbuffer.*;

public class Length {

    public static void main(String[] args) throws InterruptedException {

        //Thread.sleep(1000);
        long t1 = System.nanoTime();
        String a = "test";
        for (int i = 0; i < 1000000; i++) {
//10m loops
            if (a != null && a.length() == 0) {
                
            }
            System.out.println("i = " + i );
        }
        long t2 = System.nanoTime();
        System.out.println("Execution time: " + ((t2 - t1) * 1e-6) + " milliseconds");
        //Execution time: 351.801063 milliseconds
    }
}

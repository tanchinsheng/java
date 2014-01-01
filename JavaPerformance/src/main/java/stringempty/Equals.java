package stringempty;

import stringbuffer.*;

public class Equals {

    public static void main(String[] args) throws InterruptedException {

        //Thread.sleep(1000);
        long t1 = System.nanoTime();
        String a = "test";
        for (int i = 0; i < 200000000; i++) {
//10m loops
            if (a != null && a.equals("")) {
            }
        }
        long t2 = System.nanoTime();
        System.out.println("Execution time: " + ((t2 - t1) * 1e-6) + " milliseconds");
        //Execution time: 1838.649988 milliseconds
    }
}

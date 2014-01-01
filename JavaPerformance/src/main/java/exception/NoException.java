package exception;

import stringempty.*;
import stringbuffer.*;

public class NoException {

    public static void main(String[] args) throws InterruptedException {

        //Thread.sleep(1000);
        long t1 = System.nanoTime();
        Object obj = null;
        for (int i = 0; i < 100000; i++) {
            if (obj != null) {
                obj.hashCode();
            }
        }
        long t2 = System.nanoTime();
        System.out.println("Execution time: " + ((t2 - t1) * 1e-6) + " milliseconds");
        //Execution time: 1838.649988 milliseconds
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package length;
/**
 *
 * @author tachinse
 */
public class LessTime {

    public static void main(String[] args) throws InterruptedException {
        long t1 = System.nanoTime();
       // StopWatch stopWatch = new LoggingStopWatch("StringAddConcat");
        byte x[] = new byte[10000000];
        int length = x.length;
        for (int i = 0; i< length; i++) {
            for (int j = 0; j< length; j++) {
            }
        }
        long t2 = System.nanoTime();
        System.out.println("Execution time: " + ((t2 - t1) * 1e-6) + " milliseconds");
       // stopWatch.stop();
    }
}

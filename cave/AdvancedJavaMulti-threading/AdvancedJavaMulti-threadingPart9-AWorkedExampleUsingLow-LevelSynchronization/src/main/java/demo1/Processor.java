package demo1;
/*
In this tutorial we’ll look at how to implement the producer-consumer pattern using “low level” techniques; 
namely, wait, notify and synchronized. This isn’t the best way to implement a producer-consumer pattern 
in Java (see tutorial 7 for the best way); but this tutorial will help you to understand 
how to use wait and notify.
 */

import java.util.LinkedList;
import java.util.Random;

public class Processor {

    private final LinkedList<Integer> list = new LinkedList<>();
    private final int LIMIT = 10;
    private final Object lock = new Object();

    public void produce() throws InterruptedException {
        int value = 0;
        while (true) {
            synchronized (lock) {
                while (list.size() == LIMIT) {
                    lock.wait(); // WAIT AA
                }
                list.add(value++);
                lock.notify(); //NOTIFY BB
            }
        }
    }

    public void consume() throws InterruptedException {
        Random random = new Random();
        while (true) {
            synchronized (lock) {
                while (list.size() == 0) {
                    lock.wait(); // WAIT BB
                }
                System.out.print("List size is: " + list.size());
                int value = list.removeFirst();
                System.out.println("; value is: " + value);
                lock.notify(); // NOTIFY AA
            }
            Thread.sleep(random.nextInt(1000));
        }
    }
}

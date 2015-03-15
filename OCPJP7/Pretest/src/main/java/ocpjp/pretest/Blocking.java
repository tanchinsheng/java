package ocpjp.pretest;

import java.util.concurrent.*;
import java.util.*;

class Blocking {

    Deque<String> gangOfFour = new LinkedBlockingDeque<String>();

    class Producer extends Thread {

        String[] authors = {"E Gamma", "R Johnson", "R Helm", "J Vlissides"};

        public void run() {
            for (String author : authors) {
                gangOfFour.add(author);
                try {
                    // take time to add
                    Thread.sleep(1000);
                } catch (InterruptedException ie) {
                    // ignore it
                }
            }
        }
    }

    class Consumer extends Thread {

        int numOfAuthors = 4;
        int processedAuthors = 0;

        public void run() {
            while (processedAuthors < 4) {
                while (gangOfFour.isEmpty()) { /*wait till an entry is inserted*/ }
                System.out.println(gangOfFour.remove());
                processedAuthors++;
            }
        }
    }

    public static void main(String[] args) {
        Blocking blocking = new Blocking();
        blocking.new Producer().start();
        blocking.new Consumer().start();
    }
}
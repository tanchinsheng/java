package com.lynda.javatraining.threads.interrupt;

public class MyThread extends Thread {

    @Override
    public void run() {
        int iterations = 5;

        try {
            for (int i = 0; i < iterations; i++) {
                System.out.println("From secondary thread");
                sleep(1000);
            }
        } catch (InterruptedException e) {
            System.err.println("Thread interrupted");
        }
    }

}

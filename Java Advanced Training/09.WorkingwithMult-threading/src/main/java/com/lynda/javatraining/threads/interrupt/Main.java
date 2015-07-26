package com.lynda.javatraining.threads.interrupt;

public class Main {

    public static void main(String[] args) {

        MyThread t = new MyThread();
        t.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException interruptedException) {

        }
        t.interrupt();
        System.out.println("Called interrupt() from main thread");
    }

}

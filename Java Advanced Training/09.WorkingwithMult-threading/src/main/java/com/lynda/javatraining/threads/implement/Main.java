package com.lynda.javatraining.threads.implement;

public class Main {

    public static void main(String[] args) {

        int iterations = 3;

        MyThread thread = new MyThread();
        thread.start();

        MyRunnable runnable = new MyRunnable();
        new Thread(runnable).start();

        try {
            for (int i = 0; i < iterations; i++) {
                System.out.println("from main process");
                Thread.sleep(3000);
            }
        } catch (InterruptedException e) {
            System.err.println(e);
        }

    }

}

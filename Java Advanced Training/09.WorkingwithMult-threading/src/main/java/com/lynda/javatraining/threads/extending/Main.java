package com.lynda.javatraining.threads.extending;

public class Main {

    public static void main(String[] args) {

        int iterations = 3;
        MyThread thread = new MyThread();
        thread.start();
        try {
            for (int i = 0; i < iterations; i++) {
                System.out.println("from main process");
                Thread.sleep(3000);
            }
        } catch (InterruptedException interruptedException) {
            System.err.println(interruptedException);
        }

    }

}

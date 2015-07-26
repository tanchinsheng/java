package com.lynda.javatraining.threads.sync;

public class Main {

    public static void main(String[] args) {
        TargetClass target = new TargetClass();
        MyThread t1 = new MyThread(1, target);
        MyThread t2 = new MyThread(2, target);
        MyThread t3 = new MyThread(3, target);
        t1.start();
        t2.start();
        t3.start();
    }

}

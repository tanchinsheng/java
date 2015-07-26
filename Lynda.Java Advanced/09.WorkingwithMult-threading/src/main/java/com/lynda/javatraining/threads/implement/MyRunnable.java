/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lynda.javatraining.threads.implement;

/**
 *
 * @author cstan
 */
public class MyRunnable implements Runnable {

    @Override
    public void run() {
        int iterations = 5;

        try {
            for (int i = 0; i < iterations; i++) {
                System.out.println("From runnable thread");
                Thread.sleep(2000);
            }
        } catch (InterruptedException e) {
            System.err.println(e);
        }
    }

}

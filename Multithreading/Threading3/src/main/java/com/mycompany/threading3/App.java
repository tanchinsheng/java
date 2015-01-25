package com.mycompany.threading3;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Hello world!
 *
 */
public class App {

    public static void main(String[] args) {

        Thread thread1 = new Thread(new Runnable() {//anonynomous class

            @Override
            public void run() {
                for (int i = 0; i < 5; i++) {
                    try {
                        System.out.println("Hello " + i);
                        Thread.sleep(100);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
        thread1.start();
    }
}

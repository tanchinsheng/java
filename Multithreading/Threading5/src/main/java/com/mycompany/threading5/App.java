package com.mycompany.threading5;

import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

class Processor extends Thread {

    private volatile int counter = 0; // volatile cannot be cached locally by thread

    @Override
    public void run() {
        while (counter == 0) {
            System.out.println("Running");
            try {
                Thread.sleep(100);
            } //super.run();
            catch (InterruptedException ex) {
                Logger.getLogger(Processor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public void shutdown() {
        counter++;
    }
}

/**
 * Hello world!
 *
 */
public class App {

    public static void main(String[] args) {
        System.out.println("Hello World!");
        Processor pro = new Processor();
        pro.start();
        new Scanner(System.in).nextLine();
        pro.shutdown();

    }
}
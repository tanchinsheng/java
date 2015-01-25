package com.mycompany.theading4;

import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

class Processor extends Thread {

    private volatile boolean running = true; // volatile cannot be cached locally by thread
    
    @Override
    public void run() {
        while (running) {
            System.out.println("Running");
            try {
                Thread.sleep(100);
            } //super.run();
            catch (InterruptedException ex) {
                Logger.getLogger(Processor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }
    public void shutdown(){
        running = false;
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
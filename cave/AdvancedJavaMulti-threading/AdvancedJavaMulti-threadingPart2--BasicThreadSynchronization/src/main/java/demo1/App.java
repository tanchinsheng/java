/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package demo1;

import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

//Gracefully terminating a thread from another thread:

class Processor extends Thread {
    // volatile prevents thread caching.
    private volatile boolean running= true; 
    @Override
    public void run() {
        while (running) {
            System.out.println("Hello");
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                Logger.getLogger(Processor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    public void shutdown(){
        running = false;
    }
}

public class App {
    public static void main(String[] args) {
        Processor p1 = new Processor();
        p1.start();
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
        p1.shutdown();
    }
}

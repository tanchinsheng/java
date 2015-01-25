/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.waitnotify;

import java.util.Scanner;

/**
 *
 * @author cstan
 */
public class Processor {
    public void produce() throws InterruptedException {
        //synchronized statements
        synchronized(this){ // sync on processor's object itself
            // run only on acquiring the intrinic lock of this object
            System.out.println("Producer thread running....");
            wait();// belongs to object
            // called only from synchronized method
            // no consume system resource
            System.out.println("Resumed.");
        }
        
    }
    public void consume() throws InterruptedException {
        
        Scanner scanner = new Scanner(System.in);
        Thread.sleep(2000);
        synchronized(this){ 
            System.out.println("Waiting for return key");
            scanner.nextLine();
            System.out.println("Return key pressed");
            notify();
            Thread.sleep(5000);
        }
    }   
    
}

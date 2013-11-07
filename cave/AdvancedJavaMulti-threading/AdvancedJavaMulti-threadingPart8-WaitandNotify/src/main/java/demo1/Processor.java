/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package demo1;

import java.util.Scanner;
 
 
public class Processor {
 
    public void produce() throws InterruptedException {
        //intrinic lock of Processor's object
        synchronized (this) {
            System.out.println("Producer thread running ....");
            wait();//no consume resource, called only in sychchronized block,  unlike sleep()
            System.out.println("Resumed.");
        }
    }
 
    public void consume() throws InterruptedException {       
        Scanner scanner = new Scanner(System.in);
        Thread.sleep(2000);       
        synchronized (this) {
            System.out.println("Waiting for return key.");
            scanner.nextLine();
            System.out.println("Return key pressed.");
            notify();// can call only in synchronized code block
            Thread.sleep(5000);
        }
    }
}
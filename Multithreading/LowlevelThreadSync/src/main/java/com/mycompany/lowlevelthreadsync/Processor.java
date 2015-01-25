/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.lowlevelthreadsync;

import java.util.LinkedList;
import java.util.Random;

/**
 *
 * @author cstan
 */
public class Processor {

    private LinkedList<Integer> list = new LinkedList<Integer>();
    private final int LIMIT = 10;
    private final Object lock = new Object();

    public void produce() throws InterruptedException {
        int value = 0;

        while (true) {
            synchronized (lock) {
                while (list.size() == LIMIT){
                    lock.wait();
                }
                list.add(value++);
                System.out.println("List size is =  " + list.size());
                lock.notify();
            }
        }

    }

    public void consume() throws InterruptedException {

        Random random = new Random();
        
        while (true) {
            synchronized (lock) {
                while (list.size()==0){
                    lock.wait();
                }
                System.out.print("List size is: " + list.size());
                int value = list.removeFirst();
                System.out.println("; removed value is :" + value);
                lock.notify();
            }
            Thread.sleep(random.nextInt(1000));
        }
    }
}

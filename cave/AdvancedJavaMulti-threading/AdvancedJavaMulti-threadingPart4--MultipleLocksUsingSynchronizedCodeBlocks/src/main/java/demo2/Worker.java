/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package demo2;

import java.util.Random;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author cstan
 */
public class Worker {

    private final List<Integer> list1 = new ArrayList<>();
    private final List<Integer> list2 = new ArrayList<>();

    // acquire intrinsic/monitor lock, only one lock for one object
    public synchronized void stageOne() {
        try {
            Thread.sleep(1);
        } catch (InterruptedException ex) {
            Logger.getLogger(Worker.class.getName()).log(Level.SEVERE, null, ex);
        }
        list1.add(new Random().nextInt(100));
    }

    public synchronized void stageTwo() {
        try {
            Thread.sleep(1);
        } catch (InterruptedException ex) {
            Logger.getLogger(Worker.class.getName()).log(Level.SEVERE, null, ex);
        }
        list2.add(new Random().nextInt(100));
    }

    public void process() {
        for (int i = 0; i < 1000; i++) {
            stageOne();
            stageTwo();
        }
    }

    public void main() {

        System.out.println("starting..");
        long start = System.currentTimeMillis();

        //Example5, shared list1, list2 
//Time taken : 4084
//list1: 2000, list2: 2000
        Thread t1 = new Thread(new Runnable() {
            public void run() {
                process();
            }
        });
        t1.start();
        Thread t2 = new Thread(new Runnable() {
            public void run() {
                process();
            }
        });
        t2.start();
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(Worker.class.getName()).log(Level.SEVERE, null, ex);
        }  
        
        long end = System.currentTimeMillis();
        System.out.println("Time taken : " + (end - start));
        System.out.println("list1: " + list1.size() + ", list2: " + list2.size());
    }

}

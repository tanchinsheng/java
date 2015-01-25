/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.producerconsumer1;

/**
 *
 * @author cstan
 */
public class ProducerConsumerExample {
    public static void main(String[] args) {
        Drop drop = new Drop();
        (new Thread(new Producer(drop))).start();
        (new Thread(new Consumer(drop))).start();
    }
}

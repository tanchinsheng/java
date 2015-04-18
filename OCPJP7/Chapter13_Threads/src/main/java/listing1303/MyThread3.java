/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package listing1303;

/**
 *
 * @author cstan
 */
public class MyThread3 implements Runnable {

    @Override
    public void run() {
        System.out.println("In run method: thread name is :" + Thread.currentThread().getName());
    }

    public static void main(String[] args) {
        Thread myThread = new Thread(new MyThread3());
        myThread.run();
        System.out.println("In main method: thread name is :" + Thread.currentThread().getName());
    }

}

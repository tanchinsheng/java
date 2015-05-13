/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q46;

public class MyThread extends Thread {

    @Override
    public void run() {
        try {
            // waiting for the thread itself to terminate
            this.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("In run method, thread name is: " + Thread.currentThread().getName());
    }

    public static void main(String[] args) {
        Thread myThread = new MyThread();
        //Thread myThread = new Thread(new MyThread());
        System.out.println("In main method, thread name is: " + Thread.currentThread().getName());
        myThread.start();
    }

}

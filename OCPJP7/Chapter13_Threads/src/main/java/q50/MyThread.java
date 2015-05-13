/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q50;

public class MyThread extends Thread {

    @Override
    public void run() {
        System.out.println("Burp! ");
    }

    public static void main(String[] args) throws InterruptedException {
        Thread myThread = new MyThread();
        myThread.start();
        System.out.println("Eat! ");
        myThread.join();
        System.out.println("Run! ");
    }

}

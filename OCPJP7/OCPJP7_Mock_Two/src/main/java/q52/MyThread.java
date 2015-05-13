/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q52;

public class MyThread extends Thread {

    @Override
    public void run() {
        System.out.println("Running");
    }

    public static void main(String[] args) {
        Runnable r = new MyThread();
        Thread myThread = new Thread(r);
        myThread.start();
    }

}

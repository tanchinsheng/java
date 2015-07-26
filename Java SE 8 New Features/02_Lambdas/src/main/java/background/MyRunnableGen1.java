/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package background;

/**
 *
 * @author cstan
 */
class MyRunnable1 implements Runnable {

    @Override
    public void run() {
        System.out.println("Hello from Gen 1!");
    }

}

public class MyRunnableGen1 {

    public static void main(String[] args) {
        MyRunnable1 r = new MyRunnable1();
        new Thread(r).start();
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package question.time;

/**
 *
 * @author cstan
 */
public class ThreadTest12 {

    public static void main(String[] args) {
        Thread t1 = new Thread() {
            synchronized public void run() { // implicit lock = this
                System.out.print("t1");
                try {
                    wait(5000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        };
        Thread t2 = new Thread() {
            synchronized public void run() {
                System.out.print("t2");
                try {
                    wait(5000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        };
        t1.start();
        t2.start();
        System.out.print("main");
    }

}

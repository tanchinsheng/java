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
public class ThreadTest {

    public static void main(String[] args) {
        Thread t1 = new Thread() {
            public void run() {
                System.out.print("t1");
            }
        };
        Thread t2 = new Thread() {
            public void run() {
                System.out.print("t2");
            }
        };
        t1.start();
        t1.sleep(5000);
        t2.start();
        t2.sleep(5000);
        System.out.print("main");
    }

}

/*
 * Starting threads using the Thread constructor with anonymous classes:
 */
package demo3;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author cstan
 */
public class App {

    public static void main(String[] args) {
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    System.out.println("Hello" + i);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
        t1.start();
    }
}

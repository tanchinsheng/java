/*
 * Starting threads using the Thread class directly:
 */

package demo2;

import java.util.logging.Level;
import java.util.logging.Logger;

class Runner implements Runnable {

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            System.out.println("Hello" + i);
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                Logger.getLogger(Runner.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}

public class App {

    public static void main(String[] args) {
     Thread t1 = new Thread(new Runner());
     Thread t2 = new Thread(new Runner());
    }
}

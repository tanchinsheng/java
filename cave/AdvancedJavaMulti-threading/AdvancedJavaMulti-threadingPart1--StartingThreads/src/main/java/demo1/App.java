package demo1;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Starting threads by extending the Thread class:
 *
 */
class Runner extends Thread {

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
        Runner runner1 = new Runner();
        runner1.start();
        Runner runner2 = new Runner();
        runner2.start();
    }
}

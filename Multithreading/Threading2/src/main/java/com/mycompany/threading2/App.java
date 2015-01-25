package com.mycompany.threading2;

import java.util.logging.Level;
import java.util.logging.Logger;

class Runner implements Runnable{
    // implements Runnable.
    //employs a Runnable object, is more general, 
    // because the Runnable object can subclass a class other than Thread
    //this approach more flexible
    @Override
    public void run() {
        for (int i=0; i<5;i++){
            try {
                System.out.println("Hello " + i);
                Thread.sleep(100);
            }

            catch (InterruptedException ex) {
                Logger.getLogger(Runner.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}

/**
 * 
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        Thread thread1 = new Thread(new Runner());
        thread1.start();
        
        Thread thread2 = new Thread(new Runner());
        thread2.start();
        
        (new Thread(new Runner())).start();
        
    }
}

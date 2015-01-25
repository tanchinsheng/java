package com.mycompany.threading1;

import java.util.logging.Level;
import java.util.logging.Logger;



class Runner extends Thread {
    //extending Thread class
    //is easier to use in simple applications, 
    //but is limited by the fact that your task class must be a descendant of Thread
    @Override
    public void run() {
        for (int i=0; i<5;i++){
            try {
                System.out.println("Hello " + i);
                Thread.sleep(100);
            }
            //super.run();
            catch (InterruptedException ex) {
                Logger.getLogger(Runner.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        //super.run();
    }
    
}

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        Runner runner1 = new Runner();
        runner1.start();
        
        Runner runner2 = new Runner();
        runner2.start();
        
        (new Runner()).start();
        
    }
}

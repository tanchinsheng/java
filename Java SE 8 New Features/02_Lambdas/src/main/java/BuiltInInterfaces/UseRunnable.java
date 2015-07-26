package BuiltInInterfaces;

import java.util.logging.Level;
import java.util.logging.Logger;

public class UseRunnable {

    public static void main(String[] args) {

//        Runnable r1 = new Runnable() {
//            @Override
//            public void run() {
//                System.out.println("Running Thread 1");
//            }
//        };
//
//        Runnable r2 = new Runnable() {
//            @Override
//            public void run() {
//                System.out.println("Running Thread 2");
//            }
//        };
        Runnable r1 = () -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(UseRunnable.class.getName()).log(Level.SEVERE, "Interrupted !", ex);
            }
            System.out.println("Running Thread 1");
        };
        Runnable r2 = () -> System.out.println("Running Thread 2");
        new Thread(r1).start();
        new Thread(r2).start();

    }

}

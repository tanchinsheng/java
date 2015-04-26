/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package listing1411;

/**
 *
 * @author cstan
 */
public class BlockerQueueTest1 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        final BlockerQueue blockerQueue = new BlockerQueue(2);
        new Thread() {
            @Override
            public void run() {
                System.out.println("Thread1: attempting to remove an item from the queue ");
                blockerQueue.remove();
            }
        }.start();
        new Thread() {
            @Override
            public void run() {
                System.out.println("Thread2: attempting to insert an item to the queue ");
                blockerQueue.insert("one");
            }
        }.start();
    }

}

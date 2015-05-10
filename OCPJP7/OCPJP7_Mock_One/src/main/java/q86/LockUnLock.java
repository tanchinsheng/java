/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q86;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockUnLock {

    public static void main(String[] args) {
        Lock lock = new ReentrantLock();
        try {
            System.out.println("Lock 1");
            lock.lock();
            System.out.println("Critical section 1 ");
            System.out.println("Lock 2");
            lock.lock(); // LOCK_2
            System.out.println("Critical section 2 ");
        } finally {
            lock.unlock();
            System.out.println("Unlock 2 ");
            lock.unlock(); // UNLOCK_1
            System.out.println("Unlock 1 ");
        }
    }

}

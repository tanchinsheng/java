package com.mycompany.atomicinteger;

/**
 * Hello world!
 *
 */

import java.util.concurrent.atomic.AtomicInteger;

class AtomicCounter {
    //prevent thread interference without resorting to synchronization
    private AtomicInteger c = new AtomicInteger(0);

    public void increment() {
        c.incrementAndGet();
    }

    public void decrement() {
        c.decrementAndGet();
    }

    public int value() {
        return c.get();
    }
    
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
    }    
    

}
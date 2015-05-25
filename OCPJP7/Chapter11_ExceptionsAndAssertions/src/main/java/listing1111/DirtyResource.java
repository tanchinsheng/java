/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package listing1111;

import java.util.InputMismatchException;

public class DirtyResource implements AutoCloseable {

    /**
     * Need to call this method if you want to access this resource
     *
     * @throws RuntimeException no matter how you call this method
     *
     */
    public void accessResource() {
        // throw new RuntimeException("I wanted to access this resource. Bad luck. Its dirty resource !!!");
    }

    public void testResource() {
        throw new InputMismatchException("InputMismatch resource !!!");
    }

    /**
     * The overridden closure method from AutoCloseable interface
     *
     * @throws Exception which is thrown during closure of this dirty resource
     *
     */
    @Override
    public void close() throws Exception {
        throw new NullPointerException("Remember me. I am your worst nightmare !! I am Null pointer exception !!");
    }
}

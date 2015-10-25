/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q497.exceptions;

/**
 *
 * @author cstan
 */
public class SomeException extends Exception {

    /**
     * Creates a new instance of <code>SomeException</code> without detail
     * message.
     */
    public SomeException() {
    }

    /**
     * Constructs an instance of <code>SomeException</code> with the specified
     * detail message.
     *
     * @param msg the detail message.
     */
    public SomeException(String msg) {
        super(msg);
    }
}

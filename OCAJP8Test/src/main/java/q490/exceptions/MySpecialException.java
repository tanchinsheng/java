/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q490.exceptions;

/**
 *
 * @author cstan
 */
public class MySpecialException extends RuntimeException {

    /**
     * Creates a new instance of <code>MySpecialException</code> without detail
     * message.
     */
    public MySpecialException() {
    }

    /**
     * Constructs an instance of <code>MySpecialException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public MySpecialException(String msg) {
        super(msg);
    }
}

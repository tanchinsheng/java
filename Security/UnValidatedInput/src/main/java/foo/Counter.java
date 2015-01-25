/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package foo;

/**
 *
 * @author cstan
 */
public class Counter {

    private static int count;

    public static synchronized int getCount() {
        count++;
        return count;
    }
}
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package generictype;

/**
 * Generic version of the Box class.
 *
 * @param <T> the type of the value being boxed
 */
public class Box<T> {

    // T stands for "Type"
    private T t;

    public void set(T t) {
        this.t = t;
    }

    public T get() {
        return t;
    }
}

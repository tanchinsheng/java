/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author cstan
 * @param <T>
 */
// T is placeholder
public class List<T> {

    private final T[] datastore;
    private final int size;
    private int pos;

    public List(int numElements) {
        size = numElements;
        pos = 0;
        datastore = (T[]) new Object[size];
    }

    public void add(T element) {
        datastore[pos] = element;
        ++pos;
    }

    @Override
    public String toString() {
        String elements = "";
        for (int i = 0; i < pos; ++i) {
            elements += datastore[i] + " ";
        }
        return elements;
    }
}

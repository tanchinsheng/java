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
public class StringList<T> implements IList<T> {

    private final T[] datastore;
    private final int size;
    private int pos;

    public StringList(int numElements) {
        size = numElements;
        pos = 0;
        datastore = (T[]) new Object[size];
    }

    @Override
    public void add(T elements) {
        datastore[pos] = elements;
        ++pos;
    }

    @Override
    public T get(int n) {
        return datastore[n];
    }

    public static void main(String[] args) {

    }

}

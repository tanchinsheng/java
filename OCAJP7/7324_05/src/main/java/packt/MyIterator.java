package packt;

import java.util.Iterator;

// implements the Iterator interface and supports a trivial iteration
public class MyIterator implements Iterator<Integer> {

    private int value;
    private final int size;

    public MyIterator() {
        value = 1;
        size = 10;
    }

    @Override
    public boolean hasNext() {
        return value <= size;
    }

    @Override
    public Integer next() {
        return value++;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}

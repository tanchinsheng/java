package packt;

import java.util.Iterator;

// uses MyIterator to support its use in a for-each statement
public class MyIterable implements Iterable<Integer> {

    private MyIterator iterator;

    public MyIterable() {
        iterator = new MyIterator();
    }

    @Override
    public Iterator<Integer> iterator() {
        return iterator;
    }

}
